<?xml version="1.0" encoding="ISO-8859-1"?>
<ncl id="composerConnBase" xmlns="http://www.ncl.org.br/NCL3.0/CausalConnectorProfile" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.ncl.org.br/NCL3.0/CausalConnectorProfile http://www.ncl.org.br/NCL3.0/profiles/NCL30CausalConnector.xsd">
	<!-- A sintaxe dos nomes dos conectores causais segue a seguinte terminologia:
     * Primeiro sao definidas as condicoes (conditions), sempre precedidas do termo "on";
       - onBegin: ao comecar a exibicao da ancora;
       - onEnd: ao terminar a exibicao da ancora;
       - onMouseSelection: ao ser selecionada a ancora pelo mouse;
       - onKeySelection: ao ser selecionada a ancora por uma chave do teclado(ou controle remoto);
       - etc.
     * Em seguida sao definidas as avaliacoes (assessments), sempre seguidas do termo $ e depois Test, 
       onde $ eh nulo ou um numeral qualquer para identificar papeis com mesmos testes de avaliacao;
       - TrNomeDaTransicao$Test: para testes efetuados sobre valores de tempos de transicao;
       - AttState$Test: para testes efetuados sobre valores de estado dos eventos;
       - AttRepeat$Test: para testes efetuados sobre valores do atributo repeat de um evento. 
       - AttOccurences$Test: para testes efetuados sobre valores do atributo occurences de um evento. 
       - AttNode$Test: para testes efetuados sobre valores de atributos dos nos. 
     * Sobre os comparadores para teste, a menos que explicitamente comentado, eh assumido "eq".
     * Apos sao definidas as acoes (actions);
     * Condicoes, avaliacoes e acoes sao sempre seguidas de 1 ou N: 
       - 1 significa que apenas um ator pode exercer aquele papel; 
       - N significa que qualquer numero de atores pode exercer o papel.
     * Sobre os qualificadores (qualifiers), a menos que explicitamente comentado, eh assumido:
       - Nos papeis "condition" e "assessment" o valor eh "any";
       - Nos papeis "action" o valor eh "all".
     * Quando mais de uma acao eh especificada, elas sao aplicadas em sequencia, a menos quando 
       comentado o contrario;
     * Acoes precedidas por "Delay" devem esperar o retardo especificado para serem disparadas.
-->
	<head>
		<connectorBase>
			
			<!-- Conectores Causais 1 para 1 -->
			<!-- OnBegin -->
			
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou
			                de mídia (ou uma âncora de um nó de mídia)  
					 Ação:
			                Inicia um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginStart">
				<simpleCondition role="onBegin"/>
				<simpleAction role="start"/>
			</causalConnector>
			
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Encerra um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->	
			<causalConnector id="onBeginStop">
				<simpleCondition role="onBegin"/>
				<simpleAction role="stop"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Aborta um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->	
			<causalConnector id="onBeginAbort">
				<simpleCondition role="onBegin"/>
				<simpleAction role="abort"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Pausa um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginPause">
				<simpleCondition role="onBegin"/>
				<simpleAction role="pause"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Retoma a apresentação de um nó de contexto, 
			                de alternativa ou de mídia (ou uma âncora de 
			                um nó de mídia) a partir do momento em que
			                ele foi pausado
			 		-->
			<causalConnector id="onBeginResume">
				<simpleCondition role="onBegin"/>
				<simpleAction role="resume"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para
			                o conector) a uma propriedade
			 		-->
			<causalConnector id="onBeginSet">
				<connectorParam name="var"/>
				<simpleCondition role="onBegin"/>
				<simpleAction role="set" value="$var"/>
			</causalConnector>
			
			<!-- OnEnd -->
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndStart">
				<simpleCondition role="onEnd"/>
				<simpleAction role="start"/>
			</causalConnector>
			
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndStop">
				<simpleCondition role="onEnd"/>
				<simpleAction role="stop"/>
			</causalConnector>
			
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Aborta um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndAbort">
				<simpleCondition role="onEnd"/>
				<simpleAction role="abort"/>
			</causalConnector>
			
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Pausa um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndPause">
				<simpleCondition role="onEnd"/>
				<simpleAction role="pause"/>
			</causalConnector>
			
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Retoma a apresentação de um nó de contexto, 
			                de alternativa ou de mídia (ou uma âncora de 
			                um nó de mídia) a partir do momento em que
			                ele foi pausado
			 		-->
			<causalConnector id="onEndResume">
				<simpleCondition role="onEnd"/>
				<simpleAction role="resume"/>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para
			                o conector) a uma propriedade
			 		-->
			<causalConnector id="onEndSet">
				<connectorParam name="var"/>
				<simpleCondition role="onEnd"/>
				<simpleAction role="set" value="$var"/>
			</causalConnector>
			
			<!-- OnEnterSelection -->
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStart">
				<simpleCondition role="onSelection"/>
				<simpleAction role="start"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStop">
				<simpleCondition role="onSelection"/>
				<simpleAction role="stop"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Aborta um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionAbort">
				<simpleCondition role="onSelection"/>
				<simpleAction role="abort"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Pausa um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionPause">
				<simpleCondition role="onSelection"/>
				<simpleAction role="pause"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Retoma a apresentação de um nó de contexto, 
			                de alternativa ou de mídia (ou uma âncora de 
			                um nó de mídia) a partir do momento em que
			                ele foi pausado
			 		-->
			<causalConnector id="onSelectionResume">
				<simpleCondition role="onSelection"/>
				<simpleAction role="resume"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para
			                o conector) a uma propriedade
			 		-->
			<causalConnector id="onSelectionSet">
				<connectorParam name="var"/>
				<simpleCondition role="onSelection"/>
				<simpleAction role="set" value="$var"/>
			</causalConnector>
			
			<!-- OnKeySelection -->
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionStart">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="start"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionStop">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="stop"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Aborta um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionAbort">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="abort"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Pausa um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionPause">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="pause"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Retoma a apresentação de um nó de contexto, 
			                de alternativa ou de mídia (ou uma âncora de 
			                um nó de mídia) a partir do momento em que
			                ele foi pausado
			 		-->
			<causalConnector id="onKeySelectionResume">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="resume"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Atribui o valor $var (passado como parâmetro para
			                o conector) a uma propriedade
			 		-->
			<causalConnector id="onKeySelectionSet">
				<connectorParam name="keyCode"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="set" value="$var"/>
			</causalConnector>
			
			<!-- Conectores Causais 1 para N -->
			<!-- OnBegin -->
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginStartN">
				<simpleCondition role="onBegin"/>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginStopN">
				<simpleCondition role="onBegin"/>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Aborta N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginAbortN">
				<simpleCondition role="onBegin"/>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Pausa N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginPauseN">
				<simpleCondition role="onBegin"/>
				<simpleAction role="pause" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Retoma a apresentação de N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de 
			                um nó de mídia) a partir do momento em que
			                ele foi pausado
			 		-->
			<causalConnector id="onBeginResumeN">
				<simpleCondition role="onBegin"/>
				<simpleAction role="resume" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para
			                o conector) a N propriedades
			 		-->
			<causalConnector id="onBeginSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onBegin"/>
				<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onBeginStartNStopN">
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e pausa N nós de contexto, de alternativa
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onBeginStartNPauseN">
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e retoma a partir do momento em que foi 
			                pausado N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) de forma 
			                sequencial
			 		-->
			<causalConnector id="onBeginStartNResumeN">
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) e atribui
			                o valor $var (passado como parâmetro para o conector) 
			                a N propriedades de forma sequencial
			 		-->
			<causalConnector id="onBeginStartNSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e termina com um retardo de $delay (passado
			                como parâmetro para o conector) N nós de contexto,
			                de alternativa ou de mídia (ou uma âncora de um nó
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onBeginStartNDelayStopN">
				<connectorParam name="delay"/>
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e inicia N nós de contexto, de alternativa
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onBeginStopNStartN">
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e pausa N nós de contexto, de alternativa
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onBeginStopNPauseN">
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e retoma a partir do momento em que foi 
			                pausado N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) de forma 
			                sequencial
			 		-->
			<causalConnector id="onBeginStopNResumeN">
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) e atribui
			                o valor $var (passado como parâmetro para o conector) 
			                a N propriedades de forma sequencial
			 		-->
			<causalConnector id="onBeginStopNSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e inicia com um retardo de $delay (passado
			                como parâmetro para o conector) N nós de contexto,
			                de alternativa ou de mídia (ou uma âncora de um nó
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onBeginStopNDelayStartN">
				<connectorParam name="delay"/>
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Inicio de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para 
                            o conector) a N propriedades e termina com um retardo 
                            de $delay (passado como parâmetro para o conector) 
                            N nós de contexto, de alternativa ou de mídia (ou 
                            uma âncora de um nó de mídia) de forma sequencial
			 		-->
			<causalConnector id="onBeginSetVarNDelayStopN">
				<connectorParam name="delay"/>
				<connectorParam name="var"/>
				<simpleCondition role="onBegin"/>
				<compoundAction operator="seq">
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			
			<!-- OnEnd -->
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndStartN">
				<simpleCondition role="onEnd"/>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndStopN">
				<simpleCondition role="onEnd"/>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Aborta N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndAbortN">
				<simpleCondition role="onEnd"/>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Pausa N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndPauseN">
				<simpleCondition role="onEnd"/>
				<simpleAction role="pause" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Retoma a partir do momento em que foi pausado N 
			                nós de contexto, de alternativa ou de mídia (ou 
			                uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndResumeN">
				<simpleCondition role="onEnd"/>
				<simpleAction role="resume" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para 
                            o conector) a N propriedades
			 		-->
			<causalConnector id="onEndSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onEnd"/>
				<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndStartNStopN">
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e pausa N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndStartNPauseN">
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e retoma a partir do momento em que foi 
			                pausado N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) de forma 
			                sequencial
			 		-->
			<causalConnector id="onEndStartNResumeN">
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Termino de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e atribui 
			                o valor $var (passado como parâmetro para o conector) 
			                a N propriedades
			 		-->
			<causalConnector id="onEndStartNSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Inicia e termina com um retardo de $delay (passado 
			                como parâmetro para o conector) N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um nó 
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onEndStartNDelayStopN">
				<connectorParam name="delay"/>
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e inicia N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onEndStopNStartN">
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e pausa N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onEndStopNPauseN">
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e retoma a partir do momento em que foi 
			                pausado N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) de forma 
			                sequencial
			 		-->
			<causalConnector id="onEndStopNResumeN">
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e atribui 
			                o valor $var (passado como parâmetro para o conector) 
			                a N propriedades de forma sequencial
			 		-->
			<causalConnector id="onEndStopNSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Termina e inicia com um retardo de $delay (passado 
			                como parâmetro para o conector) N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um nó 
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onEndStopNDelayStartN">
				<connectorParam name="delay"/>
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para 
                            o conector) a N propriedades e termina nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um nó 
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onEndSetVarNDelayStopN">
				<connectorParam name="delay"/>
				<connectorParam name="var"/>
				<simpleCondition role="onEnd"/>
				<compoundAction operator="seq">
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			
			<!-- OnEnterSelection -->
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStartN">
				<simpleCondition role="onSelection"/>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStopN">
				<simpleCondition role="onSelection"/>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Aborta N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionAbortN">
				<simpleCondition role="onSelection"/>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Pausa N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionPauseN">
				<simpleCondition role="onSelection"/>
				<simpleAction role="pause" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Retoma a partir do momento em que foi pausado N 
			                nós de contexto, de alternativa ou de mídia (ou 
			                uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionResumeN">
				<simpleCondition role="onSelection"/>
				<simpleAction role="resume" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Atribui o valor $var (passado como parâmetro para 
                            o conector) a N propriedades
			 		-->
			<causalConnector id="onSelectionSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onSelection"/>
				<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStartNStopN">
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia e pausa N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStartNPauseN">
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia e retoma a partir do momento em que foi 
			                pausado N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onSelectionStartNResumeN">
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e atribui
			                o valor de $var (passado como parâmetro para o
			                conector) a N propriedades
			 		-->
			<causalConnector id="onSelectionStartNSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Inicia e termina com um retardo de $delay (passado 
			                como parâmetro para o conector) N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um nó 
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onSelectionStartNDelayStopN">
				<connectorParam name="delay"/>
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina e inicia N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) 
			                de forma sequencial
			 		-->
			<causalConnector id="onSelectionStopNStartN">
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina e pausa N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) 
			                de forma sequencial
			 		-->
			<causalConnector id="onSelectionStopNPauseN">
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina e retoma a partir do momento em que foi
			                pausado N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) de forma 
			                sequencial
			 		-->
			<causalConnector id="onSelectionStopNResumeN">
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina N nós de contexto, de alternativa ou de
			                mídia (ou uma âncora de um nó de mídia) e atribui
			                o valor de $var (passado como parâmetro ao conector)
			                a N propriedades de forma sequencial
			 		-->
			<causalConnector id="onSelectionStopNSetN">
				<connectorParam name="var"/>
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Termina e inicia com um retardo de $delay (passado 
			                como parâmetro para o conector) N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um nó 
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onSelectionStopNDelayStartN">
				<connectorParam name="delay"/>
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla de seleção 
					 Ação:
			                Atribui o valor de $var (passado como parâmetro 
			                ao conector) a N propriedades e termina com um 
			                retardo de $delay (passado como parâmetro para 
			                o conector) N nós de contexto, de alternativa ou
			                de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onSelectionSetVarNDelayStopN">
				<connectorParam name="delay"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection"/>
				<compoundAction operator="seq">
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			
			<!-- OnKeySelection -->
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionStartN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionStopN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Aborta N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionAbortN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Pausa N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionPauseN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="pause" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Retoma a partir do momento em que foi pausado N 
			                nós de contexto, de alternativa ou de mídia (ou 
			                uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionResumeN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="resume" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Atribui o valor de $var (passado como parâmetro
			                para o conector) a N propriedades
			 		-->
			<causalConnector id="onKeySelectionSetN">
				<connectorParam name="keyCode"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStartNStopN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia e pausa N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStartNPauseN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia e retoma a partir do momento em que foi
			                pausado N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) de forma
			                sequencial
			 		-->
			<causalConnector id="onKeySelectionStartNResumeN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e atribui
			                o valor de $var (passado como parâmetro para o
			                conector) a N propriedades de forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStartNSetN">
				<connectorParam name="keyCode"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia e termina com um retardo de $delay (passado
			                como parâmetro para o conector) N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um 
			                nó de mídia) de forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStartNDelayStopN">
				<connectorParam name="keyCode"/>
				<connectorParam name="delay"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina e inicia N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStopNStartN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina e pausa N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStopNPauseN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="pause" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina e retoma a partir do momento em que foi
			                pausado N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) de forma 
			                sequencial
			 		-->
			<causalConnector id="onKeySelectionStopNResumeN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="resume" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina N nós de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e atribui
			                o valor de $var (passado como parâmetro para o 
			                conector) a N propriedades de forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStopNSetN">
				<connectorParam name="keyCode"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina e inicia com um retardo de $delay (passado
			                como parâmetro para o conector) N nós de contexto, 
			                de alternativa ou de mídia (ou uma âncora de um nó 
			                de mídia) de forma sequencial
			 		-->
			<causalConnector id="onKeySelectionStopNDelayStartN">
				<connectorParam name="keyCode"/>
				<connectorParam name="delay"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um nó de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Atribui o valor de $var (passado como parâmetro
			                para o conector) a N propriedades e termina com 
			                um retardo de $delay (passado como parâmetro para 
			                o conector) N nós de contexto, de alternativa ou 
			                de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionSetVarNDelayStopN">
				<connectorParam name="keyCode"/>
				<connectorParam name="delay"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection" key="$keyCode"/>
				<compoundAction operator="seq">
					<simpleAction role="set" value="$var" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" delay="$delay" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!-- Conectores Causais N para 1 -->
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Atribui o valor de $var (passado como parâmetro
			                para o conector) a uma propriedade
			 		-->
			<causalConnector id="onKeySelectionNSet">
				<connectorParam name="keyCode"/>
				<connectorParam name="var"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<simpleAction role="set" value="$var"/>
			</causalConnector>
			<!-- Conectores Causais N para N -->
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNStartN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNStopN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Aborta N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNAbortN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Pausa N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNPauseN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<simpleAction role="pause" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Retoma a partir do momento em que foi pausado N nós 
			                de contexto, de alternativa ou de mídia (ou uma âncora 
			                de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNResumeN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionNStartNStopN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o
			                conector
					 Ação:
			                Termina e inicia N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de 
			                forma sequencial
			 		-->
			<causalConnector id="onKeySelectionNStopNStartN">
				<connectorParam name="keyCode"/>
				<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
				<compoundAction operator="seq">
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
					<simpleAction role="start" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Início de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de mídia
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginAttNodeTestStartN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onBegin"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Início de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Termina N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginAttNodeTestStopN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onBegin"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Início de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Aborta N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onBeginAttNodeTestAbortN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onBegin"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndAttNodeTestStartN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onEnd"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Termina N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndAttNodeTestStopN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onEnd"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Aborta N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onEndAttNodeTestAbortN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onEnd"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="abort" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o conector 
			                e o valor da propriedade que assume o papel attNodeTest 
			                sendo igual a $value (passado como parâmetro para 
			                o conector)
					 Ação:
			                Inicia N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNAttNodeTestStartN">
				<connectorParam name="keyCode"/>
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="start" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Seleção de um dos nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) com
			                a tecla $keyCode passada como parâmetro para o conector 
			                e o valor da propriedade que assume o papel attNodeTest 
			                sendo igual a $value (passado como parâmetro para 
			                o conector)
					 Ação:
			                Termina N nós de contexto, de alternativa ou de mídia 
			                (ou uma âncora de um nó de mídia)
			 		-->
			<causalConnector id="onKeySelectionNAttNodeTestStopN">
				<connectorParam name="keyCode"/>
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onSelection" key="$keyCode" max="unbounded" qualifier="or"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<simpleAction role="stop" max="unbounded" qualifier="par"/>
			</causalConnector>
			<!--@doc Ativação:
			                Início de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onBeginAttNodeTestStartNStopN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onBegin"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Inicia e termina N nós de contexto, de alternativa 
			                ou de mídia (ou uma âncora de um nó de mídia) de
			                forma sequencial
			 		-->
			<causalConnector id="onEndAttNodeTestStartNStopN">
				<connectorParam name="value"/>
				<compoundCondition operator="and">
					<simpleCondition role="onEnd"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<compoundAction operator="seq">
					<simpleAction role="start" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Início de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Atribui o valor de $setValue (passado como parâmetro
			                para o conector) a N propriedades e inicia N nós de 
			                contexto, de alternativa ou de mídia (ou uma âncora
			                de um nó de mídia) de forma sequencial
			 		-->
			<causalConnector id="onBeginAttNodeTestSetNStartN">
				<connectorParam name="value"/>
				<connectorParam name="setValue"/>
				<compoundCondition operator="and">
					<simpleCondition role="onBegin"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<compoundAction operator="seq">
					<simpleAction role="set" value="$setValue" max="unbounded" qualifier="par"/>
					<simpleAction role="start" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
			<!--@doc Ativação:
			                Término de um nó de contexto, de alternativa ou de 
			                mídia (ou uma âncora de um nó de mídia) e o valor
			                da propriedade que assume o papel attNodeTest sendo
			                igual a $value (passado como parâmetro para o conector)
					 Ação:
			                Atribui o valor de $setValue (passado como parâmetro
			                para o conector) a N propriedades e termina N nós de 
			                contexto, de alternativa ou de mídia (ou uma âncora
			                de um nó de mídia) de forma sequencial
			 		-->
			<causalConnector id="onEndAttNodeTestSetNStopN">
				<connectorParam name="value"/>
				<connectorParam name="setValue"/>
				<compoundCondition operator="and">
					<simpleCondition role="onEnd"/>
					<assessmentStatement comparator="eq">
						<attributeAssessment role="attNodeTest" eventType="attribution" attributeType="nodeProperty"/>
						<valueAssessment value="$value"/>
					</assessmentStatement>
				</compoundCondition>
				<compoundAction operator="seq">
					<simpleAction role="set" value="$setValue" max="unbounded" qualifier="par"/>
					<simpleAction role="stop" max="unbounded" qualifier="par"/>
				</compoundAction>
			</causalConnector>
		</connectorBase>
	</head>
</ncl>