import { apiRequest } from "@/lib/api-client"

export async function getCrisisHub() {
  return apiRequest<any>("/crisis")
}

export async function getCopingStrategies() {
  return apiRequest<any[]>("/crisis/coping-strategies")
}

export async function detectCrisis(text: string) {
  return apiRequest<any>("/crisis/detect", {
    method: "POST",
    body: JSON.stringify({ text }),
  })
}

export async function getSafetyPlan() {
  return apiRequest<any>("/crisis/safety-plan")
}

export async function updateSafetyPlan(data: {
  warningSignals?: string[]
  copingStrategies?: string[]
  professionalContacts?: string[]
  environmentSafetySteps?: string[]
  reasonForLiving?: string
}) {
  return apiRequest<any>("/crisis/safety-plan", {
    method: "PUT",
    body: JSON.stringify(data),
  })
}
