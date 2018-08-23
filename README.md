## Scala: parser combinators на примере парсера формул

Время от времени у меня возникает желание придумать свой собственный маленький язык программирования и написать интерпретатор. В этот раз я начал писать на scala, узнал про библиотеку parser combinators, и был поражён: оказывается, можно писать парсеры легко и просто. Чтобы не превращать статью в пособие по "рисованию совы", ниже приведёна реализация разбора и вычисления выражений типа "1 + 2 * sin(pi / 2)".


Сам парсинг и вычисление выражения занимают всего лишь 44 непустых строчки — не то чтобы я сильно стремился сократить их количество, но выглядит это реально просто и лаконично. 
