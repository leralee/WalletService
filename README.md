# Wallet Service

Wallet Service — это приложение для управления балансом игрока, позволяющее регистрировать новых игроков, авторизовывать их, пополнять и снимать средства, а также просматривать историю транзакций.

## Основные функции

1. **Регистрация нового игрока**: Создание новой учетной записи с именем пользователя и паролем.
2. **Авторизация**: Вход в систему с использованием имени пользователя и пароля.
3. **Просмотр баланса**: Пользователи могут просматривать свой текущий баланс.
4. **Пополнение баланса**: Пользователи могут пополнять свой баланс.
5. **Снятие средств**: Пользователи могут снимать средства со своего баланса.
6. **Просмотр истории транзакций**: Доступ к истории транзакций для администраторов.


## Тестовый доступ для просмотра аудита

Для быстрой демонстрации функциональности просмотра аудита в приложении уже предустановлен аккаунт администратора:
- Имя пользователя: `admin`
- Пароль: `admin`

## Аутентификация

Для доступа к защищенным ресурсам API необходимо аутентифицироваться с использованием JWT (JSON Web Token).

Authorization: Bearer {YOUR_JWT_TOKEN}

На запрос с использованием "/admin/audits" нужно войти под логином и паролем выше.


### 1. Получение токена

Отправьте POST-запрос на `http://localhost:8080/login` с логином и паролем.

В ответе вы получите токен в формате:

```json
{
  "status": "success",
  "message": "Успешная авторизация",
  "token": "JWT_TOKEN"
}
```

## API

### Для выполнения запросов к API необходимо использовать действующий JWT.
### Для получения токена обратитесь к документации по аутентификации.
### При выполнении запросов токен должен передаваться в заголовке Authorization в формате:

`Authorization: Bearer YOUR_JWT_TOKEN`

1) URL: `http://localhost:8080/balances` c POST запросом
Описание: Операции с балансом (пополнение или вывод средств).

Параметры:

action: строка, действие (может быть "deposit" или "withdraw").
amount: число, сумма.
transaction_id: строка, UUID транзакции.
Пример тела запроса:

```
{
"action": "deposit",
"amount": 1000,
"transaction_id": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
}
```

URL: `http://localhost:8080/balances` c GET запросом
Описание: Получение текущего баланса авторизованного пользователя.


Ответы:

200 OK: Успешное выполнение.
201 CREATED: Успешная регистрация
400 Bad Request: Ошибки валидации или неверные параметры запроса.
401 Unauthorized: Пользователь не авторизован или токен невалиден.
500 Internal Server Error: Внутренние ошибки сервера.
GET /balances

## Для запуска PostgreSQL в Docker:

1. Убедитесь, что у вас установлен Docker и docker-compose.
2. Перейдите в каталог проекта и выполните команду:
```bash
docker-compose up -d
```
3. Для остановки и удалении всех контейнеров
```bash
docker-compose down
```



