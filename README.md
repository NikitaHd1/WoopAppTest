Добрый день, спасибо за задание, его было интересно делать, потому что я раньше уже задумывался
как бы я сделал данный свайп, но до реализации дело не дошло)
Стоит отметить хорошую работу в плане дизайна, мне понравилось то, что получилось, надеюсь вам тоже))

Из улучшений, которые я для себя вижу:
1. В FadingEdgeRecyclerView каждый раз когда вызывается onScrollChanged() я рисую тень.
На мой взгляд это не совсем правильно, т.к. мне не нужно это делать так часто. Можно добавить
что-то типа оператора debounce.
2. Возможно стоило оверайднуть метод onBackPressed() и уведомить пользователя, что при следующем
нажатии он выйдет из приложения.

Надеюсь, я ничего не забыл. Буду рад услышать ваше мнение о проделанной работе.