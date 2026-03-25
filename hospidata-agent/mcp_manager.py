from agents.mcp.server import MCPServerStreamableHttp
from dotenv import load_dotenv
import os

class MCPManager:

    @staticmethod
    def get_server():
        return MCPServerStreamableHttp(
            {
                "url": os.getenv("STOCK_MCP_SERVER")
            }
        )