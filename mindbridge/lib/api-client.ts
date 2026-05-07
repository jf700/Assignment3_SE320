"use client"

const BASE_URL = typeof window !== "undefined" ? "" : "http://localhost:8080"

function getAccessToken(): string | null {
  if (typeof window === "undefined") return null
  return localStorage.getItem("accessToken")
}

function getRefreshToken(): string | null {
  if (typeof window === "undefined") return null
  return localStorage.getItem("refreshToken")
}

export function setTokens(accessToken: string, refreshToken: string) {
  localStorage.setItem("accessToken", accessToken)
  localStorage.setItem("refreshToken", refreshToken)
}

export function clearTokens() {
  localStorage.removeItem("accessToken")
  localStorage.removeItem("refreshToken")
  localStorage.removeItem("userId")
  localStorage.removeItem("userName")
}

export function getUserId(): string | null {
  if (typeof window === "undefined") return null
  return localStorage.getItem("userId")
}

export function setUserInfo(userId: string, name: string) {
  localStorage.setItem("userId", userId)
  localStorage.setItem("userName", name)
}

export function isAuthenticated(): boolean {
  return !!getAccessToken()
}

async function refreshAccessToken(): Promise<boolean> {
  const refreshToken = getRefreshToken()
  if (!refreshToken) return false

  try {
    const response = await fetch(`${BASE_URL}/auth/refresh`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ refreshToken }),
    })
    if (!response.ok) return false

    const data = await response.json()
    setTokens(data.accessToken, data.refreshToken)
    return true
  } catch {
    return false
  }
}

export async function apiRequest<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const token = getAccessToken()
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...(options.headers as Record<string, string>),
  }
  if (token) {
    headers["Authorization"] = `Bearer ${token}`
  }

  let response = await fetch(`${BASE_URL}${path}`, { ...options, headers })

  if (response.status === 401 && token) {
    const refreshed = await refreshAccessToken()
    if (refreshed) {
      headers["Authorization"] = `Bearer ${getAccessToken()}`
      response = await fetch(`${BASE_URL}${path}`, { ...options, headers })
    } else {
      clearTokens()
      window.location.reload()
      throw new Error("Session expired. Please log in again.")
    }
  }

  if (!response.ok) {
    const errorBody = await response.json().catch(() => ({}))
    throw new Error(errorBody.message || `Request failed: ${response.status}`)
  }

  const text = await response.text()
  return text ? JSON.parse(text) : ({} as T)
}
