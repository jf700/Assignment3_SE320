import { apiRequest } from "@/lib/api-client"

export async function getWeeklyProgress() {
  return apiRequest<any>("/progress/weekly")
}

export async function getMonthlyTrends() {
  return apiRequest<any>("/progress/monthly")
}

export async function getBurnoutRecovery() {
  return apiRequest<any>("/progress/burnout")
}

export async function getAchievements() {
  return apiRequest<any[]>("/progress/achievements")
}
