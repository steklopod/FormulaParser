# Parser combinators

Пример парсинга файла `to.parse`:

<!-- code -->
```python
    read input name, country
    switch:
      country == "PT" ->
        call service "A"
        exit
      otherwise ->
        call service "B"
        switch:
          name == "unknown" ->
            exit
          otherwise ->
            call service "C"
            exit
```

В след. вид:

<!-- code -->
```scala
AndThen(
  ReadInput(List(name, country)),
  Choice(List(
    IfThen(
      Equals(country, PT),
      AndThen(CallService(A), Exit)
    ),
    OtherwiseThen(
      AndThen(
        CallService(B),
        Choice(List(
          IfThen(Equals(name, unknown), Exit),
          OtherwiseThen(AndThen(CallService(C), Exit))
        ))
      )
    )
  ))
)
```

[Parser combinator](https://en.wikipedia.org/wiki/Parser_combinator) - это просто функция, 
которая принимает парсеры в качестве входных данных и возвращает новый синтаксический анализатор 
в качестве вывода, аналогично тому, как функции более высокого порядка полагаются на вызов других функций, 
которые передаются в качестве входных данных для создания новой функции в качестве вывода.

В качестве примера, предположим, что у нас есть **парсер `int`**, который распознает целочисленные _литералы_ 
и **парсер `plus`**, который распознает символ _«+»_. 
Следовательно, мы можем **создать парсер**, 
который распознает последовательность **_`int plus int`_** как целочисленное дополнение.

Стандартная библиотека Scala включает в себя реализацию комбинаторов парсеров, 
которая размещается по адресу: [github.com/scala/scala-parser-combinators](https://github.com/scala/scala-parser-combinators)

Чтобы использовать его, вам просто потребуется следующая зависимость в вашем `build.sbt`: 
<!-- code -->
```sbtshell
    «org.scala-lang.modules» %% »scala-parser-combinators«% »1.1.1"
```

## Создание Лексера

Нам понадобятся токены для идентификаторов и строковых литералов, а также все зарезервированные ключевые 
слова и знаки препинания: 
`exit`, `read input`, `call service`, `switch`, `otherwise`, `:`, `->`, `==`, и `,`.

Нам также необходимо создавать искусственные токены, которые представляют собой увеличение и уменьшение 
в идентификации: 
`INDENT` и `DEDENT`, соответственно. 
Пожалуйста, проигнорируйте их сейчас, так как мы перейдем к ним на более позднем этапе.

<!-- code -->
```scala
    sealed trait WorkflowToken
    
    case class IDENTIFIER(str: String) extends WorkflowToken
    case class LITERAL(str: String) extends WorkflowToken
    case class INDENTATION(spaces: Int) extends WorkflowToken
    
    case object EXIT extends WorkflowToken
    case object READINPUT extends WorkflowToken
    case object CALLSERVICE extends WorkflowToken
    case object SWITCH extends WorkflowToken
    case object OTHERWISE extends WorkflowToken
    case object COLON extends WorkflowToken
    case object ARROW extends WorkflowToken
    case object EQUALS extends WorkflowToken
    case object COMMA extends WorkflowToken
    case object INDENT extends WorkflowToken
    case object DEDENT extends WorkflowToken
```

`RegexParsers` создан для построения парсеров символов с использованием `регулярных выражений`. 
Он обеспечивает неявные преобразования из `String` и `Regex` в `Parser [String]`, 
что позволяет использовать их в качестве отправной точки для составления все более сложных парсеров.

Наш лексер расширяет [RegexParsers](https://github.com/scala/scala-parser-combinators/blob/1.1.x/docs/Getting_Started.md), который является подтипом [Parsers](https://github.com/scala/scala-parser-combinators/blob/1.1.x/docs/Getting_Started.md): 

<!-- code -->
```scala
    object WorkflowLexer extends RegexParsers {
```

Начнем с указания, какие символы следует игнорировать как пробельные символы. Мы не можем игнорировать `\ n`, так как нам нужно, чтобы он распознавал уровень идентификации, определяемый количеством пробелов, которые следуют за ним. Любой другой символ пробела можно игнорировать:

<!-- code -->
```scala
    override def skipWhitespace = true
    override val whiteSpace = "[ \t\r\f]+".r
```

[примеры взяты отсюда,](https://habr.com/post/325446/)
[и отсюда](https://enear.github.io/2016/03/31/parser-combinators/)
