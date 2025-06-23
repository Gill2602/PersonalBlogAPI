# 🚀 Blog RestAPI – Endpoint Structure

## 🔐 Auth Endpoint

### `POST /api/v1/auth`

- 📨 **Description:** Returns a JWT token for authentication.
- 🔐 **Authentication:** No authentication required.  

#### 🔸 Request Body

```json
{
  "email": "example@gmail.com",
  "password": "SecretPassword"
}
```

---

## 👤 Users Endpoint

### `POST /api/v1/users`

- 🆕 **Description:** Registers a new user.
- 🔐 **Authentication:** No authentication required.  

#### 🔸 Request Body

```json
{
  "firstName": "Mario",
  "lastName": "Rossi",
  "birthDate": "2000-06-12",
  "email": "rossi.mario@gmail.com",
  "password": "Mario2000@"
}
```

### `GET /api/v1/users`

- 📥 **Description:** Returns a list of all registered users.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

### `PUT /api/v1/users/{id}`

- 📥 **Description:** Updates user data completely or partially.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

#### 🔸 Request Body

```json
{
  "firstName": "Mario",
  "lastName": "Rossi",
  "birthDate": "1999-06-12",
  "role": "ADMIN"
}
```

### `DELETE /api/v1/users/{id}`

- 📥 **Description:** Deletes a user by specifying ID.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

---

## 🗂️ Categories Endpoint

### `POST /api/v1/categories`

- 📝 **Description:** Creates a new category.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

#### 🔸 Request Body

```json
{
  "name": "Tech",
  "description": "Technology"
}
```

### `GET /api/v1/categories`

- 📥 **Description:** Returns a list of all available categories.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

### `PUT /api/v1/categories/{id}`

- 📝 **Description:** Updates category parameters.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

#### 🔸 Request Body

```json
{
  "description": "Technology"
}
```

### `DELETE /api/v1/categories/{id}`

- 📥 **Description:** Removes a category by specifying ID.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

---

## 📝 Personal Articles Endpoint

### `POST /api/v1/me/articles`

- 📄 **Description:** Creates a new article with `DRAFT` status.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

#### 🔸 Request Body

```json
{
  "title": "TOP 10 European football teams",
  "content": "According to various journalists and experts, the best football team might be Italian.",
  "categoryName": "Sport"
}
```

### `GET /api/v1/me/articles`

- 🔍 **Description:** Returns all articles of the authenticated user.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

### `PUT /api/v1/me/articles/{id}`

- ✏️ **Description:** Updates an existing article of the authenticated user.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

#### 🔸 Request Body

```json
{
  "title": "TOP 10 European football teams",
  "content": "According to various experts and journalists, the best football team might be Italian.",
  "categoryName": "Sport",
  "status": "PUBLISHED"
}
```

### `DELETE /api/v1/me/articles/{id}`

- 🔍 **Description:** Removes an article of the authenticated user.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

---

## 🌍 Global Articles Endpoint

### `GET /api/v1/articles`

- 🌐 **Description:** Returns all **published** articles.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

### `GET /api/v1/articles/admin`

- 🌐 **Description:** Returns all **published** articles with more details.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.

### `GET /api/v1/articles/user/id/{id}`

- 👤 **Description:** Returns all **published** articles by a specific user using their `ID`.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

### `GET /api/v1/articles/user/email/{email}`

- 📧 **Description:** Returns all **published** articles by a specific user using their `email`.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** USER or ADMIN.

### `DELETE /api/v1/articles/{id}/admin`

- 📧 **Description:** Deletes an article without ownership check.
- 🔐 **Authentication:** Bearer Token (JWT).
- 🛡️ **Required Role:** ADMIN.
