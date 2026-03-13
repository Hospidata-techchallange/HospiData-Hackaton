from agents import Agent, ModelSettings

class AgentsFactory:

    @staticmethod
    def create_agents():

        agentStock = Agent(
            name="EstoqueAssisente",
            model="gpt-4.1-nano",
            handoff_description="Assistente responsável pela gestão e consulta de estoque hospitalar.",
            instructions=(
            """
            Você é um assistente de gestão de estoque hospitalar.

            Use sempre as ferramentas disponíveis para responder perguntas
            sobre produtos, categorias e lotes.

            Ferramentas:
            - buscar_categorias: lista categorias
            - buscar_produtos: consulta produtos
            - buscar_lotes: consulta lotes (ordenados por validade - FEFO)

            Regras:
            - Nunca invente dados
            - Sempre priorize lotes que vencem primeiro
            - Informe produto, lote, quantidade, validade e localização

            Use ferramentas sempre que a pergunta envolver dados do estoque.
            """
            ),
            model_settings=ModelSettings(
                tool_choice="auto",
                temperature=0,
                parallel_tool_calls=False
            ),
        )

        return {
            "stock": agentStock
        }