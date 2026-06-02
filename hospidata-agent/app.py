from agents import Runner
import streamlit as st
from dotenv import load_dotenv
import asyncio, json, sys

from agent_factory import AgentsFactory
from mcp_manager import MCPManager


if sys.platform == "win32":
    asyncio.set_event_loop_policy(asyncio.WindowsProactorEventLoopPolicy())

load_dotenv()

AGENT_OPTIONS = {
    "stock": {
        "label": "Estoque",
        "title": "Hospidata Stock Assistant",
        "placeholder": "Pergunte algo sobre o estoque...",
        "spinner": "Consultando estoque..."
    },
    "appointment": {
        "label": "Agendamentos",
        "title": "Hospidata Appointment Assistant",
        "placeholder": "Pergunte algo sobre médicos, agendas ou consultas...",
        "spinner": "Consultando agendamentos..."
    }
}

# -------------------------
# AGENTS
# -------------------------

if "agents" not in st.session_state:
    st.session_state.agents = AgentsFactory.create_agents()

if "current_agent_key" not in st.session_state:
    st.session_state.current_agent_key = "stock"

if "histories" not in st.session_state:
    st.session_state.histories = {agent_key: [] for agent_key in AGENT_OPTIONS}

selected_agent_key = st.selectbox(
    "Agent",
    options=list(AGENT_OPTIONS.keys()),
    index=list(AGENT_OPTIONS.keys()).index(st.session_state.current_agent_key),
    format_func=lambda agent_key: AGENT_OPTIONS[agent_key]["label"]
)

st.session_state.current_agent_key = selected_agent_key
st.session_state.current_agent = st.session_state.agents[selected_agent_key]
st.session_state.history = st.session_state.histories[selected_agent_key]

st.markdown(
    f"<h1 style='text-align: center;'>{AGENT_OPTIONS[selected_agent_key]['title']}</h1>",
    unsafe_allow_html=True
)

# -------------------------
# RENDER HISTÓRICO
# -------------------------

for message in st.session_state.history:

    message_type = message.get("role", None) or message.get("type", None)

    if message_type == "user":
        with st.chat_message("user"):
            st.markdown(message["content"])

    elif message_type == "assistant":
        with st.chat_message("assistant"):
            st.markdown(message["content"][0]["text"])

    elif message_type == "function_call":

        if "transfer_to" not in message["name"]:
            with st.chat_message(name="tool", avatar=":material/build:"):
                st.markdown(f'LLM chamando tool **{message["name"]}**')

                with st.expander("Visualizar argumentos"):
                    st.code(message["arguments"])

    elif message_type == "function_call_output":

        try:
            obj = json.loads(message["output"])

            with st.chat_message(name="tool", avatar=":material/data_object:"):
                with st.expander("Resposta da tool"):
                    st.code(obj)

        except json.JSONDecodeError:
            continue


# -------------------------
# EXECUÇÃO DO CHAT
# -------------------------

async def resolve_chat():

    agent_key = st.session_state.current_agent_key
    current_agent = st.session_state.agents[agent_key]

    async with MCPManager.get_server(agent_key) as server:

        current_agent.mcp_servers = [server]

        result = await Runner.run(
            starting_agent=current_agent,
            input=st.session_state.history,
            context=st.session_state.history
        )

        st.session_state.agents[agent_key] = result.last_agent
        st.session_state.current_agent = result.last_agent
        st.session_state.histories[agent_key] = result.to_input_list()
        st.session_state.history = st.session_state.histories[agent_key]


# -------------------------
# INPUT
# -------------------------

prompt = st.chat_input(AGENT_OPTIONS[selected_agent_key]["placeholder"])

if prompt:

    st.session_state.history.append({
        "role": "user",
        "content": prompt
    })

    with st.chat_message("user"):
        st.markdown(prompt)

    with st.spinner(AGENT_OPTIONS[selected_agent_key]["spinner"]):

        asyncio.run(resolve_chat())
        st.rerun()


# -------------------------
# AGENT ATUAL
# -------------------------

if "current_agent" in st.session_state:
    st.toast(f"Agente atual: {st.session_state.current_agent.name}")
