# Importância de entender o ciclo de vida da atividade

É de grande importância entender o que acontece em cada momento de vida em uma atividade, sabendo disso podemos identificar possíveis gargalos, ou otimizar o desempenho do aplicativo.

# Ciclos de vida

a classe “Activity” fornece um conjunto principal de seis callbacks: [onCreate()](#oncreate), [onStart()](#onstart), [onResume()](#onresume), [onPause()](#onpause), [onStop()](#onstop) e [onDestroy()](#ondestroy). Conforme a atividade entra em um novo estado, o sistema invoca cada um desses callbacks.

![Diagrama do ciclo de vida](https://developer.android.com/guide/components/images/activity_lifecycle.png?hl=pt-br)

## Como funciona a eliminação da atividade/processo?

O sistema precisa eliminar processo quando precisa liberar RAM, e o que decide qual processo vai ser eliminado é o estado do mesmo, o processo depende do estado da ativdade, e tudo isso é levado em considerado quando isso ocorrer.

| Probabilidade de eliminação | Estado do processo                            | Estado da atividade               |
| --------------------------- | --------------------------------------------- | --------------------------------- |
| Mínimo                      | Em primeiro plano (com foco ou prestes a ter) | Criado<br/>Iniciado<br />Retomado |
| Mais                        | Segundo plano (perde o foco)                  | Pausada                           |
| Máximo                      | Segundo plano (não visível) <br/> Vazio       | Parado<br> Destruído              |

## onCreate()

**Esse callback precisa ser implementado.**

O sistema aciona ele quando cria uma atividade. Quando ela é criada, é inserido o estado ["Criado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#CREATED). Aqui executamos toda a lógica básica de inicialização do aplicativo, isso deve acontecer somente uma vez durante todo o período que a atividade durar.

Por exemplo, sua implementação de onCreate() pode vincular dados a listas, associar a atividade a um ViewModel e instanciar algumas variáveis com escopo de classe. Esse método recebe o parâmetro savedInstanceState, um objeto Bundle que contém o estado anteriormente salvo da atividade. Se a atividade nunca existiu, o valor do objeto Bundle será nulo.

Caso nosso componente esteja conectado ao ciclo de vida da sua atividade, ele receberá o evento [ON_CREATE](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_CREATE). O método anotado com @OnLifecycleEvent será chamado para que seu componente ciente do ciclo de vida possa executar qualquer código de configuração necessário para o estado criado.

Sua atividade não reside no estado ["Criado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#CREATED). Após o método onCreate() concluir a execução, a atividade insere o estado ["Iniciado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#STARTED) e o sistema chama os métodos onStart() e onResume() em rápida sucessão.

## onStart()

Quando a atividade insere o estado ["Iniciado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#STARTED), o sistema invoca esse callback. Torna a atividade visível ao usuário, à medida que o aplicativo prepara a ela para inserir o primeiro plano e se tornar interativa. Por exemplo, é nesse método que o aplicativo inicializa o código que mantém a IU.

Quando a atividade é movida para o estado ["Iniciado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#STARTED), qualquer componente ciente do ciclo de vida que esteja ligado ao ciclo de vida da atividade receberá o evento [ON_START](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_START).

O método onStart() faz a conclusão muito rapidamente, quando a finalização é feita pelo callback, a atividade insere o estado ["Retomado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#RESUMED) e o sistema invoca o método onResume().

## onResume()

Quando a atividade insere o estado ["Retomado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#RESUMED), ela vem para o primeiro plano e o sistema invoca o callback onResume(). É com esse estado que o aplicativo interage com o usuário, então a aplicação permanece nesse estado até que algo faça com que perca o foco.
Esse evento pode ser, por exemplo, receber uma chamada telefônica, navegar pelo usuário para outra atividade ou desativar a tela do dispositivo.

Quando a atividade é movida para o estado ["Retomado"](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#RESUMED), qualquer componente ciente do ciclo de vida ligado ao ciclo de vida da atividade receberá o evento [ON_RESUME](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_RESUME). Nesse momento inicializamos funcionalidades que requer que o usuário interaja, por exemplo: Câmera.

Quando ocorre um evento de interrupção, a atividade insere o estado Pausado e o sistema invoca o callback onPause().

Caso a atividade retorne do estado "Pausado" para o estado "Retomado", o sistema chamará novamente o método onResume().

Exemplo (retirado direto do developer.android) de componente ciente do ciclo de vida que acessa a câmera quando o componente recebe o evento [ON_RESUME](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_RESUME):

```kotlin
class CameraComponent : LifecycleObserver {

    ...

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun initializeCamera() {
        if (camera == null) {
            getCamera()
        }
    }

    ...
}
```

Independente de qual o evento escolhermos para inicializar algo, devemos nos atentar para que seja feita a liberação do mesmo no respectivo ciclo de vida correto.

Se inicializou algo no ON_START, devemos liberar no ON_STOP.
ON_START -> ON_STOP

Se inicializou algo no ON_RESUME, devemos liberar no ON_PAUSE.
ON_RESUME -> ON_PAUSE

## onPause()

Esse metodo é chamado como a primeira indicação de que o usuário está deixando sua atividade, embora nem sempre signifique que a atividade esteja sendo destruída. Com isso sabemos que a atividade não está mais em primeiro plano, embora ainda possa estar visível se o usuário estiver no modo de várias janelas.
Devemos usar para pausar ou ajustar operações que não devem continuar (ou que precisem continuar com moderação) enquanto a Activity estiver no modo "Pausado" e aquelas que você espera retomar em breve. Segue alguns exemplos do por que uma atividade pode estar "Pausado":

> Algum evento interromper a execução do aplicativo, esse é o caso mais comum.
>
> No Android 7.0 (API de nível 24) ou mais recentes, diversos aplicativos operam no modo de várias janelas. Como só um dos aplicativos (janelas) tem foco a qualquer momento, o sistema pausa todos os outros aplicativos.
>
> Uma nova atividade semitransparente (como uma caixa de diálogo) é aberta. Enquanto a atividade estiver parcialmente visível, mas não for a atividade em foco, ela permanecerá pausada.

Quando a atividade é movida para o estado pausado, qualquer componente ciente do ciclo de vida ligado ao ciclo de vida da atividade receberá o evento [ON_PAUSE](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_PAUSE).

Também podemos utilizar para liberar recusos do sistema, como por exemplo sensores, ou qualquer recurso que pode drenar a bateria do usuário.
No entanto, uma atividade pausada ainda poderá ser completamente visível no modo de várias janelas. Assim, considere usar [onStop()](#onstop) em vez de onPause() para liberar ou ajustar completamente operações e recursos relacionados à IU para melhorar o suporte do modo de várias janelas.

A execução onPause() é muito breve e não oferece necessariamente tempo suficiente para realizar operações de salvamento. Por isso, não devemos utilizar onPause() para salvar dados do aplicativo ou do usuário, fazer chamadas de rede ou executar transações do banco de dados. Esse tipo de trabalho pode não ser concluído antes da finalização do método.

## onStop()

Quando a atividade não estiver mais visível ao usuário, ela vai inserir o estado Interrompido e o sistema invocará o callback. Isso pode ocorrer, por exemplo, quando uma atividade recém-iniciada preenche toda a tela. O sistema também poderá chamar onStop() quando a atividade parar de operar e estiver prestes a ser concluída.

Quando a atividade é movida para o estado interrompido, qualquer componente ciente do ciclo de vida ligado ao ciclo de vida da atividade receberá o evento [ON_STOP](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_STOP). É aqui que podemos interromper qualquer atividade que não precise ser usada enquanto não estiver visível na tela.

Devemos utilizar o onStop para liberar ou ajustar recursos desnecssarios, enquanto o aplicativo não estiver visível ao usuário. Exemplos:

- Pausar animações
- Alternar de atualizações de local mais específicas para as menos detalhadas
- Abortar requisições
- Operações de desligamento de uso intensivo da CPU.
- Salvar conteúdo em um armazenamento persistente

Exemplo retirado diretamente do developer.android:

```kotlin
override fun onStop() {
    // call the superclass method first
    super.onStop()

    // save the note's current draft, because the activity is stopping
    // and we want to be sure the current note progress isn't lost.
    val values = ContentValues().apply {
        put(NotePad.Notes.COLUMN_NAME_NOTE, getCurrentNoteText())
        put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle())
    }

    // do this update in background on an AsyncQueryHandler or equivalent
    asyncQueryHandler.startUpdate(
            token,     // int token to correlate calls
            null,      // cookie, not used here
            uri,       // The URI for the note to update.
            values,    // The map of column names and new values to apply to them.
            null,      // No SELECT criteria are used.
            null       // No WHERE columns are used.
    )
}
```

Quando sua atividade entra no estado "Parado", o objeto Activity é mantido residente na memória: ele mantém todas as informações de estado e membro, mas não é anexado ao gerenciador de janelas. Quando a atividade é retomada, ela chama novamente essas informações. Não é necessário reiniciar componentes criados durante qualquer método de callback que leve ao estado "Retomado". O sistema também acompanha o estado atual de cada objeto View no layout. Portanto, se o usuário inserir um texto em um widget EditText, o conteúdo será retido e você não precisará salvar e restaurar.

> Observação: quando a atividade for interrompida, o sistema poderá destruir o processo que contém a atividade se precisar recuperar a memória. Mesmo que o sistema destrua o processo quando a atividade estiver interrompida, ele ainda manterá o estado dos objetos View (como o texto em um widget EditText) em um Bundle (um blob de pares de chave-valor) e os restaurará caso o usuário navegue de volta à atividade.

No estado de "Parado", a atividade volta a interagir com o usuário ou para de operar e é encerrada. Se a atividade voltar, irá ser executado onRestart(). Caso a Activity deixe de operar, irá ser executado onDestroy().

## onDestroy()

É chamado antes de a atividade ser destruída.

Devido a atividade está sendo finalizada (pelo fato do usuário descartá-la completamente ou devido a finish() ser chamado na atividade); ou
o sistema está destruindo temporariamente a atividade devido a uma mudança na configuração (como a rotação do dispositivo ou o modo de várias janelas)

Quando a atividade é movida para o estado [destruído](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State#DESTROYED), qualquer componente ciente do ciclo de vida ligado ao ciclo de vida da atividade receberá o evento [ON_DESTROY](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.Event#ON_DESTROY). Devemos aproveitar esse momento para limpar qualquer item antes de destruir a atividade.

É possível distinguir entre essas duas situações com o método [isFinishing()](<https://developer.android.com/reference/android/app/Activity#isFinishing()>).

Se estiver sendo encerrado, onDestroy() será o callback do ciclo de vida final recebido pela atividade. Se onDestroy() for chamado como o resultado da mudança na configuração, o sistema criará imediatamente uma nova instância de atividade e chamará onCreate() nessa instância na nova configuração.

Os callbacks onDestroy() liberarão todos os recursos ainda não liberados pelos callbacks anteriores, como [onStop()](#onstop).
