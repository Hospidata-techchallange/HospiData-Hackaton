from mcp.server.fastmcp import FastMCP
import pymysql
import json
import os
from dotenv import load_dotenv

load_dotenv()

mcp = FastMCP("hospidata-stock-mcp", dependencies=["pymysql"])

DB_HOST = os.getenv("DB_HOST")
DB_PORT = int(os.getenv("DB_PORT", 3306))
DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")


def get_connection():
    return pymysql.connect(
        host=DB_HOST,
        port=DB_PORT,
        user=DB_USER,
        password=DB_PASSWORD,
        database=DB_NAME,
        cursorclass=pymysql.cursors.DictCursor
    )


@mcp.tool()
def buscar_categorias(active: bool | None = None):
    """
    Busca categorias de medicamentos e materiais hospitalares no estoque.

    Parâmetros:
    - active (bool, opcional): filtra categorias ativas ou inativas.
    """

    try:
        with get_connection() as conn:
            with conn.cursor() as cursor:

                query = """
                    SELECT 
                        tc.id_category,
                        tc.name,
                        tc.description,
                        tc.active
                    FROM stock_schema.tb_category tc
                """

                params = []

                if active is not None:
                    query += " WHERE tc.active = %s"
                    params.append(active)

                cursor.execute(query, params)

                rows = cursor.fetchall()

                for item in rows:
                    if isinstance(item["active"], (bytes, bytearray)):
                        item["active"] = item["active"] == b'\x01'

        return {
            "count": len(rows),
            "categories": rows
        }
    except Exception as e:
        return {"error": str(e)}
    
@mcp.tool()
def buscar_produtos(
    name: str | None = None,
    sku_code: str | None = None,
    category_id: str | None = None,
    active: bool | None = None
):
    """
    Busca produtos hospitalares e medicamentos cadastrados no estoque.

    Filtros disponíveis:
    - name (str, opcional): busca produtos que contenham o texto informado no nome.
    - sku_code (str, opcional): busca um produto pelo código SKU.
    - category_id (str, opcional): retorna produtos pertencentes a uma categoria específica.
    - active (bool, opcional): filtra produtos ativos ou inativos.

    Use esta ferramenta para localizar produtos no estoque.
    """

    try:
        with get_connection() as conn:
            with conn.cursor() as cursor:

                query = """
                    SELECT
                        tp.id_product,
                        tp.name,
                        tp.description,
                        tp.sku_code,
                        tp.min_stock_alert,
                        tp.category_id,
                        tc.name as category_name,
                        tp.active
                    FROM stock_schema.tb_product tp
                    INNER JOIN stock_schema.tb_category tc
                        ON tc.id_category = tp.category_id
                    WHERE 1=1
                """

                params = []

                if name:
                    query += " AND tp.name LIKE %s"
                    params.append(f"%{name}%")

                if sku_code:
                    query += " AND tp.sku_code = %s"
                    params.append(sku_code)

                if category_id:
                    query += " AND tp.category_id = %s"
                    params.append(category_id)

                if active is not None:
                    query += " AND tp.active = %s"
                    params.append(active)

                cursor.execute(query, params)

                rows = cursor.fetchall()

                for item in rows:
                    if isinstance(item["active"], (bytes, bytearray)):
                        item["active"] = item["active"] == b'\x01'

        return {
            "count": len(rows),
            "products": rows
        }

    except Exception as e:
        return {"error": str(e)}
    

@mcp.tool()
def buscar_lotes(
    product_ids: list[str] | None = None,
    batch_number: str | None = None,
    aisle: str | None = None,
    active: bool | None = None,
    near_expiration_days: int | None = None,
    expired: bool | None = None
):
    """
    Busca lotes de produtos no estoque hospitalar.

    Os resultados são ordenados automaticamente pela data de validade mais próxima
    (princípio FEFO - First Expire First Out).

    Filtros disponíveis:

    - product_ids (list[str], opcional):
        retorna lotes de múltiplos produtos.
    
    IMPORTANTE:
    Sempre que precisar buscar lotes de mais de um produto,
    utilize este campo em vez de chamar a ferramenta várias vezes.

    Exemplo:
    product_ids = ["id1","id2","id3"]

    - batch_number (str, opcional):
        busca um lote específico.

    - aisle (str, opcional):
        filtra por corredor do estoque.

    - active (bool, opcional):
        retorna apenas lotes ativos ou inativos.

    - near_expiration_days (int, opcional):
        retorna lotes que vencem dentro de X dias.

    - expired (bool, opcional):
        quando true retorna apenas lotes vencidos.
    """

    try:
        with get_connection() as conn:
            with conn.cursor() as cursor:

                query = """
                SELECT 
                    tb.id_batch,
                    tb.batch_number,
                    tb.quantity_available,
                    tb.initial_quantity,
                    tb.manufacturing_date,
                    tb.expiration_date,
                    tb.product_id,
                    tp.name as product_name,
                    tb.location_id,
                    tl.aisle,
                    tl.bin,
                    tl.shelf,
                    tl.description,
                    tb.unit_price,
                    tb.active
                FROM stock_schema.tb_batch tb
                INNER JOIN stock_schema.tb_product tp
                    ON tp.id_product = tb.product_id
                INNER JOIN stock_schema.tb_location tl
                    ON tl.id_location = tb.location_id
                WHERE 1=1
                """

                params = []

                # Produto único
                # if product_id:
                #     query += " AND tb.product_id = %s"
                #     params.append(product_id)

                # Múltiplos produtos
                if product_ids:
                    placeholders = ",".join(["%s"] * len(product_ids))
                    query += f" AND tb.product_id IN ({placeholders})"
                    params.extend(product_ids)

                if batch_number:
                    query += " AND tb.batch_number = %s"
                    params.append(batch_number)

                if aisle:
                    query += " AND tl.aisle = %s"
                    params.append(aisle)

                if active is not None:
                    query += " AND tb.active = %s"
                    params.append(active)

                if near_expiration_days:
                    query += """
                    AND tb.expiration_date <= DATE_ADD(CURDATE(), INTERVAL %s DAY)
                    """
                    params.append(near_expiration_days)

                if expired:
                    query += " AND tb.expiration_date < CURDATE()"

                query += " ORDER BY tb.expiration_date ASC"

                cursor.execute(query, params)

                rows = cursor.fetchall()

        return {
            "count": len(rows),
            "batches": rows
        }

    except Exception as e:
        return {"error": str(e)}