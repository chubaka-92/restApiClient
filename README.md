# restApiClient
вкратце:
    Микросервис в зависимости от входящего запроса выполнял:
    - Post запрос - принимал информацию о персоне(имя, возраст, емаил), если данные были корректные то сохранял персону в БД и возвращал ID созданной записи в бд. ИНаче сообщение об ошибке(что не так) 
    -Get запрос - возвращающий инфу по всем персонам
    -Get запрос - возвращающий инфу по id персоны из запрос
    -Get запрос - поиск по имени или емейлу.  возвращающий список персон с совпадениями
    -Delete запрос - удаляет персону по id
    -Put запрос -  обновляет персону если в теле запроса указан id существующей персоны
    
