from agents.mcp.server import MCPServerStreamableHttp
from dotenv import load_dotenv
import os

class MCPManager:

    @staticmethod
    def get_server(agent_key: str):
        servers = {
            "stock": MCPManager.get_stock_server,
            "appointment": MCPManager.get_appointment_server
        }

        if agent_key not in servers:
            raise ValueError(f"Agent MCP server not configured: {agent_key}")

        return servers[agent_key]()

    @staticmethod
    def get_stock_server():
        return MCPServerStreamableHttp(
            {
                "url": os.getenv("STOCK_MCP_SERVER")
            }
        )

    @staticmethod
    def get_appointment_server():
        return MCPServerStreamableHttp(
            {
                "url": os.getenv("APPOINTMENT_MCP_SERVER")
            }
        )
