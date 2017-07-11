/*------------------------------------------------------------------------------------------------------------------------*/

1.3.9

+ RealTimeActivity agora salva imagem PNG com data e hora
+ ChartActivity grafico 3, de pizza, agora salva imagem como PNG
+ arquivos json separados em pasta unica no diretorio raiz
+ arquivos UML criados pela biblioteca PlantUML, acessiveis somente pelo Android Studio
+ Metodos de troca de atividade como onStart() e onPause para garantir que o app finalize e chame o checkFirebase()
+ feedMultiple() substituido por checkFirebase() para adicionar diretamente do Firebase, atualizando no ritmo do NodeMCU
+ firebase2 para ler timeStamp em nodo separado do consumo, que vem do NodeMCU, e funcionar no grafico
+ Iniciada integracao com NodeMCU via Firebase, na versao anterior pegava so o valor do timestamp do servidor Firebase
+ Metodo fillMonths() para BarChart e PieChart deixando eles com mesmos valores
! README.md alterado com diversas informacoes
! Cor de vermelhor para roxo no grafico 1
! Vendor.addDate() alterado para [yyyy-MM-dd]
! Vendor.addTime() alterado para  "[" + h + "h" + m + "m" + s + "s]" e com uso apenas no RealTimeActivity
! ajustes de layout
! alterado RealTimeActivity nas strigs de 3s para 1s, thread deletada, pega direto do Firebase apenas
- removido botao logout do layout land do charts e realtime
- feedMultiple() removido pois agora não insere nada via Android ou thread
- removido timestamp apenas para o Toast no RealTimeActivity

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.8

+ Firebase integrado, apesar de ja existir as dependencias adicionadas, para o RealTimeActivity
+ Grafico de Tempo Real com logica funcionando, atualiza a cada 1s mediante o Firebase
+ Salva o Server.TIMESTAMP diretamente pelo app integrado com o tempo de leitura do consumo
! Layout alterado para graficos e textos ficarem centralizados, nao sei pq eles bugaram
! Alterado de 1 segundo para 3 segundos a leitura
! Alterado o a var consumo de String para float em RealTimeActivity e no Firebase de "5" para apenas 5
! Ajuste para strings mililitros no titulo do grafico
! as constantes do AppConfig foram para Constants
! Vendor e ValueFormatter movidos para pacote utils
! pacote helper alterado para sql
! ChartActivity com toast minimizado em poucas linhas, porem mais complexo para debugar
! ChartActivity LineChart agora exibe de 1 a 30 ou de 31 a 60 dias dependendo do DataSet clicado
- pacote chart removido
- removido o AppConfig
- removido o pacote app e o AppController foi para o inico do pacote, nao esta mais dentro de app

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.7

+ comecando integracao com Firebase, criada classe Constants mas ainda nao foi feito nada
+ grafico em tempo real em nova classe RealTimeActivity
+ Mensagem de erro no login e registro caso o sistema esteja indisponivel (o php nao valide)
! alguns ajustes de layout
! classe Vendor agora faz o addRandom(), addDate(), addFormat...
! var now alterada para date em ChartActivity
! modificado o nome do arquivo "changes.c" para "MODIFICACOES.c"
! atrualizadas as bibliotecas

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.6

+ Vendor addDate, addFolder
+ Graficos salvam como PNG na pasta "/DCIM/HydroFlow"
+ styles.xml <item name="android:windowBackground">@null</item>
+ verifica permissao (WRITE) antes sempre
! AppVendor alterado para Vendor e pacote alterado de app para chart
! activity_chart layout port e land alterados, port com aviso no top sobre clique rapido e longo
! build.gradle alterado para retornar void e evitar um erro de inspection
- removido da grade a biblioteca de mask que nao era usada
- removidos os assets (fontes) ja que nao eram utilizadas (HydroFlow\v1.3.6\app\src\main)
- removido tudo que chamava as Fontes dos assets (em ChartActivity)

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.5

+ ValueFormatter.class criada para os graficos
+ DecimalFormat para a ChartActivity
! ajustes diversos com classes, codigos, coisas nao utilizadas foram retiradas ou organizadas
! ChartActivity - Layout land e portrait modificados
! ChartActivity - 4 graficos (dados de forma randômica com Float "0.0")
! ChartActivity - Comentarios diversos organizados no código
! ChartActivity - melhorias do código 

(basicamente foram feitos implementos somente no que diz respeito aos graficos incluindo código e layout)

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.4

#precisa add verificacao para "CPF, TEL, CEP" com caracteres todos preenchidos na RegisterActivity

+ appVendor Toast 	- foram modificadas todas as atividades que podem ser simplificadas
+ appVendor Intent 	- foram modificadas todas as atividades que podem ser simplificadas
+ ChartActivity - incuídos 3 graficos como ScrollView na mesma atividade
+ ChartActivity - Layout Land
! MainActivity: modificado layout
! Todas as atividade tem layout RelativeLayout
! Toast - todas mensagens centralizadas pelo AppVendor

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.3
+ RegisterActivity codigo para Mask
+ activity_register android:inputType="phone" para CPF, CEP e TELEFONE 
+ Atualizado o Logo e todos os PNGs
+ activity_splash.xml com nova imagem
- activity_register android:inputType="number" e android:digits="0123456789-. " para CPF e CEP
	DB	1.1
/*------------------------------------------------------------------------------------------------------------------------*/

1.3.2

+ activity_register.xml rejustado
+ activity_register.xml etiquetas flutuantes
+ MainActivity: NOME, EMAIL, CPF antes dos dados
+ MainActivity: added exibit versao App
- activity_register.xml btn Login retirado
- RegisterActivity: login session retirado
- LoginActivity: 	login session retirado
/*------------------------------------------------------------------------------------------------------------------------*/

1.3.1

+ PHP 1.3.1
+ Layout Registro com todas os campos
! RegisterActivity (PHP 1.3.1)
! LoginActivity (PHP 1.3.1)
! Modificadas verificacoes de login session
! Alterado no PHP para exibit DATA_ATUALIZADO
! LOG (avisos modificados com ##### e mais coisas)

/*------------------------------------------------------------------------------------------------------------------------*/

1.3.0

+ 	PHP 1.3
+ 	Layout Registro com todas os campos
! 	RegisterActivity 	(PHP 1.3)
! 	LoginActivity 		(PHP 1.3)

/*------------------------------------------------------------------------------------------------------------------------*/

1.2

+ Splash
+ Manifest mexido pra Splash
! Registro com login automatico

/*------------------------------------------------------------------------------------------------------------------------*/

1.1

Login 		OK
Registro	OK
Main		OK
Validacoes 	OK (comentadas)
DB ../android/androidhive/

/*------------------------------------------------------------------------------------------------------------------------*/
