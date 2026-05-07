import { apiRequest } from "@/lib/api-client"

export async function getEntries(page = 0, size = 20) {
  return apiRequest<any>(`/diary/entries?page=${page}&size=${size}`)
}

export async function createEntry(data: {
  situation: string
  automaticThought: string
  emotions?: { emotion: string; intensity: number }[]
  distortionIds?: string[]
  alternativeThought?: string
  moodBefore?: number
  moodAfter?: number
}) {
  return apiRequest<any>("/diary/entries", {
    method: "POST",
    body: JSON.stringify(data),
  })
}

export async function getEntryDetail(entryId: string) {
  return apiRequest<any>(`/diary/entries/${entryId}`)
}

export async function deleteEntry(entryId: string) {
  return apiRequest<void>(`/diary/entries/${entryId}`, {
    method: "DELETE",
  })
}

export async function suggestDistortions(thought: string) {
  return apiRequest<any[]>("/diary/distortions/suggest", {
    method: "POST",
    body: JSON.stringify({ thought }),
  })
}

export async function getInsights() {
  return apiRequest<any>("/diary/insights")
}
