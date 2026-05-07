import { apiRequest, setTokens, setUserInfo, clearTokens } from "@/lib/api-client"

interface AuthResponse {
  accessToken: string
  refreshToken: string
  expiresIn: number
  userId: string
  name: string
}

interface LoginRequest {
  email: string
  password: string
}

interface RegisterRequest {
  name: string
  email: string
  password: string
}

export async function login(data: LoginRequest): Promise<AuthResponse> {
  const result = await apiRequest<AuthResponse>("/auth/login", {
    method: "POST",
    body: JSON.stringify(data),
  })
  setTokens(result.accessToken, result.refreshToken)
  setUserInfo(result.userId, result.name)
  return result
}

export async function register(data: RegisterRequest): Promise<AuthResponse> {
  const result = await apiRequest<AuthResponse>("/auth/register", {
    method: "POST",
    body: JSON.stringify(data),
  })
  setTokens(result.accessToken, result.refreshToken)
  setUserInfo(result.userId, result.name)
  return result
}

export async function logout(): Promise<void> {
  try {
    await apiRequest("/auth/logout", { method: "POST" })
  } finally {
    clearTokens()
  }
}
