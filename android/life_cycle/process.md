# Processos

Na grande maioria dos casos cada aplicativo do Android, é executado no próprio processo do Linux. O processo é criado para o aplicativo quando o código precisa ser executado, e permanece em execução até que não seja mais necessário.

## Características de um processo

É importante entendermos que o ciclo de vida em um processo não é controlado diretamente pelo aplicativo. Ele é determinado pelo sistema por meio de algumas combinações, sendo elas:

- Partes do aplicativo que o sistema sabe que está sendo executado
- Importância para o usuário
- Quantidade de memória disponível no sistema

Precisamos entender bem como cada processo é afetado pelos componentes: Activity, Service e BroadcastReceiver afeta o ciclo de vida, para que não tenhs bugs/problemas por conta de estar fazendo algo em um ciclo de vida errado, por que o sistema pode eliminar o processo no meio da execução dessa tarefa, e consequentemente levar a bugs.

## Quando esse processo é eliminado?

O Android quando precisar eliminar um processo para aliviar a memória para outro aplicativo utilizar, define uma "hierarquia de importância" baseada nos componentes que está sendo executado e no estado desse componente.

Ao decidir como classificar um processo, o sistema baseará a decisão no nível mais importante encontrado entre todos os componentes ativos no processo, segue baixo como funciona essa ordem de "hierarquia de importância":

### Processo em primeiro plano

É aquele que o usuário está interagindo no momento, e temos alguns componentes que são considerado em primeiro plano de formas diferentes, sendo:

- Activity com a qual o usuário está interagindo, chamou o metódo [onResume()](./activity.md#onresume)
- BroadcastReceiver sendo executado no momento, ou seja o metódo BroadcastReceiver.onReceive() está em execução.
- Service está sendo executado em um dos callbacks: Service.onCreate(), Service.onStart() ou Service.onDestroy().

Só vai existir alguns processos em primeiro plano, e normalmente eles só vão ser removido se a memória do sistema estiver tão baixa que não é possível continuar em execução.

### Processo vísivel

Está realizando algo que o usuário de certa maneira está ciente, então encerrar pode causar impacto negativo na experiência. Sendo que é considerado um "processo vísivel":

- Executando uma Activity que está visível na tela, mas que não está em primeiro plano, basicamente se surgir uma caixa de diálogo que o usuário consegue ver a atividade anterior por trás dela. Nesse momento o metódo [onPause()](./activity.md#onpause) foi chamado na atividade anterior.
- Possuí um Service rodando em primeiro plano por meio do Service.startForeground(), que basicamente pede para que o sistema trate esse serviço como algo que o usuário está ciente ou essecialmente visível.
- Hospeda um serviço que o sistema está usando para algum recurso especifico do qual o usuário precisa estar ciente, por exemplo: plano de fundo interativo.

O número de processos que existem "vísivel" é menos limitado que os de primeiro plano, mas ainda é relativamente controlado. Esse processo é considerado extremamente importante e não vai ser eliminado, a menos que eliminar esse processo signifique manter um processo de primeiro plano em execução.

### Processo de serviço

São todos que é iniciado através do metódo Service.startService(). Esses processos não são visíveis para o usuário, mas normalmente estão fazendo algo que o usuário necessita, exemplo: Upload, Download. Sendo assim o sistema sempre mantém esses processos em execução, a menos que não tenha memória suficiente para ter todos os processos em [primeiro plano](#processo-em-primeiro-plano) e [vísiveis](#processo-vísivel).

Processos de serviço que estão em execução a mais de 30 minutos, pode ser rebaixado para permitir que ele seja enviado para a lista de LRU. É feito para prevenir situações como: vazamento de memória, ou consumindo muita memória RAM que consequentemente impede do sistema fazer um uso eficiente de processos em cache.

### Processo armazenado em cache

Normalmente ele não é necessario no momento, portanto o sistema está com passe livre para eliminar ele quando a memória for necessaria em outra parte. Esses são os unicos processos envolvido no gerenciamento de memória, quando temos um sistema bem executado normalmente vai ter varios em cache, para que consiga alterar entre os aplicativos de forma rápida e consequentemente eliminando os processos mais antigos no cache. Somente se o sistema estiver em uma situação muito critica vai eliminar todos os processos em cache, e começar a eliminar os processos de serviço, e assim por diante.

Geralmente os processos armazenado em cache possuí: instâncias de Activity que não está visível para o usuário.

Esses processos são mantidos em uma lista de pseudo-LRU, sendo que o último processo na lista é o primeiro a ser eliminado.
