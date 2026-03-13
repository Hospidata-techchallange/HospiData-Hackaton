from agents import Runner
import streamlit as st
from dotenv import load_dotenv
import asyncio, json, sys

from agent_factory import AgentsFactory
from mcp_manager import MCPManager


if sys.platform == "win32":
    asyncio.set_event_loop_policy(asyncio.WindowsProactorEventLoopPolicy())

load_dotenv()

st.markdown("<h1 style='text-align: center;'>Hospidata Stock Assistant</h1>", unsafe_allow_html=True)

# -------------------------
# HISTÓRICO
# -------------------------

if "history" not in st.session_state:
    st.session_state.history = []

# -------------------------
# RENDER HISTÓRICO
# -------------------------

for message in st.session_state.history:

    type = message.get("role", None) or message.get("type", None)

    match type:

        case "user":
            with st.chat_message("user"):
                st.markdown(message["content"])

        case "assistant":
            with st.chat_message("assistant"):
                st.markdown(message["content"][0]["text"])

        case "function_call":

            if "transfer_to" not in message["name"]:
                with st.chat_message(name="tool", avatar=":material/build:"):
                    st.markdown(f'LLM chamando tool **{message["name"]}**')

                    with st.expander("Visualizar argumentos"):
                        st.code(message["arguments"])

        case "function_call_output":

            try:
                obj = json.loads(message["output"])

                with st.chat_message(name="tool", avatar=":material/data_object:"):
                    with st.expander("Resposta da tool"):
                        st.code(obj)

            except:
                continue


# -------------------------
# AGENTS
# -------------------------

if "agentStock" not in st.session_state:

    agents = AgentsFactory.create_agents()

    st.session_state.agentStock = agents["stock"]
    st.session_state.current_agent = agents["stock"]


# -------------------------
# EXECUÇÃO DO CHAT
# -------------------------

async def resolve_chat():

    async with MCPManager.get_server() as server:

        st.session_state.agentStock.mcp_servers = [server]

        result = await Runner.run(
            starting_agent=st.session_state.current_agent,
            input=st.session_state.history,
            context=st.session_state.history
        )

        st.session_state.current_agent = result.last_agent
        st.session_state.history = result.to_input_list()


# -------------------------
# INPUT
# -------------------------

prompt = st.chat_input("Pergunte algo sobre o estoque...")

if prompt:

    st.session_state.history.append({
        "role": "user",
        "content": prompt
    })

    with st.chat_message("user"):
        st.markdown(prompt)

    with st.spinner("Consultando estoque..."):

        asyncio.run(resolve_chat())
        st.rerun()


# -------------------------
# AGENT ATUAL
# -------------------------

if "current_agent" in st.session_state:
    st.toast(f"Agente atual: {st.session_state.current_agent.name}")