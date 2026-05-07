import { apiRequest, getUserId } from "@/lib/api-client"

export async function getSessionLibrary() {
  const userId = getUserId()
  return apiRequest<any[]>(`/sessions?userId=${userId}`)
}

export async function startSession(sessionId: string) {
  return apiRequest<any>(`/sessions/${sessionId}/start`, {
    method: "POST",
  })
}

export async function chat(sessionId: string, message: string) {
  return apiRequest<any>(`/sessions/${sessionId}/chat`, {
    method: "POST",
    body: JSON.stringify({ message }),
  })
}

export async function endSession(sessionId: string, reason?: string) {
  return apiRequest<any>(`/sessions/${sessionId}/end`, {
    method: "POST",
    body: JSON.stringify({ reason: reason || "completed" }),
  })
}

export async function getSessionHistory() {
  return apiRequest<any[]>("/sessions/history")
}
