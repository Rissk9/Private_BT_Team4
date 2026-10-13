# Banking Backend API — Teammate Manual

## What this is
Spring Boot banking backend with JWT auth + Swagger docs. Every endpoint is documented at `http://localhost:8080/swagger-ui/index.html`.

---

## Before you start
Run the app: `.

```
## 1. Register a new user (public, no JWT needed)
POST `/auth/register`
Body:
```json
{
  "name": "Alice",
  "email": "alice@test.com",
  "phoneNumber": "+911234567890",
  "password": "Pass@123"
}
```
Response: `"Registration request received. OTP sent."`
→ Check console/logs for the OTP (or your OTP service output).

## 2. Verify OTP (public)
POST `/auth/verify-otp`
Body:
```json
{
  "email": "alice@test.com",
  "otp": "123456"
}
```
Response: `"Registration completed successfully"`

## 3. Log in (public) — get JWT
POST `/auth/login`
Body:
```json
{
  "username": "alice@test.com",
  "password": "Pass@123"
}
```
Response contains `accessToken`. **Copy it.**

---

## 4. Use Swagger (authorize with JWT)
Open: `http://localhost:8080/swagger-ui/index.html`

1. Click **Authorize** (lock icon top-right)
2. Type: `Bearer <paste-access-token-here>`
3. Click Authorize → Close

Now every protected endpoint works automatically with your JWT.

---

## 5. Test protected endpoints with Swagger
- `GET /users/me` — your profile
- `PUT /users/me` — update profile
- `GET /customer/status` — your customer status
- `POST /customer/profile` — submit KYC
- `POST /auth/logout` — invalidate token (needs JWT)

---

## 6. Admin: create a staff member
**Important:** You must log in with an account that has `ADMIN` role (not CUSTOMER). If you only have CUSTOMER, ask an admin for temporary admin access or create one manually in DB.

POST `/admin/employees`
Headers: `Authorization: Bearer <admin-jwt>`
Body:
```json
{
  "username": "bob.staff@bank.com",
  "email": "bob.staff@bank.com",
  "phoneNumber": "+919876543210",
  "password": "StaffPass@1"
}
```
Response: `201 CREATED` with `EmployeeResponseDto`. The created user gets `STAFF` role automatically.

**Note:** If you get 403, your JWT is from a non-ADMIN user. Log out and log in with an ADMIN account.

---

## 7. Forgot password
POST `/auth/forgot-password`
Body: `{"identifier":"alice@test.com"}`
Check console/logs for OTP, then use `POST /auth/reset-password` with identity + OTP + new password.

---

## Quick troubleshooting
| Problem | Fix |
|---|---|
| `401` on protected endpoint | Authorize Swagger with JWT (Bearer <token>) |
| `403` on `/admin/employees` | Log in with ADMIN account, not CUSTOMER |
| "Username already exists" | User already in DB; pick new username |
| No OTP in console | Check your OTP service log / email mock |

---
## Swagger URLs
- UI: `http://localhost:8080/swagger-ui/index.html`
- JSON spec: `http://localhost:8080/v3/api-docs`
- OpenAPI config: see `SwaggerConfig.java`

---
## Security notes (don't change these)
- `csrf` disabled (pre-existing)
- JWT stateless sessions
- Swagger endpoints are public for testing; business endpoints stay protected
- No secrets in docs; passwords in examples are fake
