# Pasta RES

Contém todos os recursos que não são códigos (e.g. .java, .kt), como layouts XML, strings de UI e imagens de bitmap. Esta pasta é dividida em subdiretórios. Os arquivos contidos nas subpastas desta pasta são processados ​​pelo aapt (Android Asset Packaging Tool), portanto os arquivos destas subpstas devem ser referenciados no aplicativo usando um resource ID da classe R.

- values
  Principalmente utilizado para a definicão de algumas configuracoes/tokens que podemos utilizar no aplicativo,
  assim a gente não precisa passar o valor inteiro, somente o nome desse token/configuracão, e.g:

  arquivo: colors.xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
  	<color name="purple_200">#FFBB86FC</color>
  	<color name="purple_500">#FF6200EE</color>
  	<color name="purple_700">#FF3700B3</color>
  	<color name="teal_200">#FF03DAC5</color>
  	<color name="teal_700">#FF018786</color>
  	<color name="black">#FF000000</color>
  	<color name="white">#FFFFFFFF</color>
  </resources>
  ```

  arquivo: strings.xml

  Nesse arquivo o principal objetivo seria definir "strings"/textos que podemos reutilizar em varias partes do aplicativo,
  sendo assim se precisarmos alterar por exemplo o texto do botão, e estamos utilizando em 10 telas. Se colocarmos esse texto
  em cada uma dessas telas, vamos ter que alterar nas 10, mas como colocarmos esse texto em um token nesse arquvio quando alteramos ele
  automaticamente vai refletir nas 10 telas do aplicativo.

  ```xml
  <resources>
  	<string name="app_name">Mugiwara</string>
  </resources>
  ```

- drawable
  Onde insere todas as imagens, icones, etc. Para o projeto

- layout
  Onde criamos os arquivos XMLs que formam o layout das telas de nossa aplicação. Também podemos ter modificadores específicos para diferentes telas, mostrando layouts diferentes para cada tipo de tela.
