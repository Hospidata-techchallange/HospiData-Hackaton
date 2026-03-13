from agents.mcp import MCPServerStdio

class MCPManager:
    
    @staticmethod
    def get_server():
        return MCPServerStdio(
            params={
                "command": "mcp",
                "args": ["run", "servers/stock-mcp-server.py"]
            }
        )