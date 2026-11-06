const API_BASE = "/api";

let credentials = null;

export function setApiCredentials(username, password) {
  credentials = btoa(
    String.fromCharCode(
      ...new TextEncoder().encode(`${username}:${password}`)
    )
  );
}

export function clearApiCredentials() {
  credentials = null;
}

export async function apiRequest(endpoint, options = {}) {
  const headers = new Headers(options.headers);

  if (credentials) {
    headers.set("Authorization", `Basic ${credentials}`);
  }

  if (options.body && !(options.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    headers,
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error(`API request failed: HTTP ${response.status} (${options.method || "GET"} ${endpoint})`);
  }

  const contentType = response.headers.get("content-type") || "";

  if (contentType.includes("application/json")) {
    return response.json();
  }

  return response.blob();
}

export const getMothers = (search = "") =>
  apiRequest(`/mothers${search ? `?search=${encodeURIComponent(search)}` : ""}`);
export const getDashboardSummary = () => apiRequest("/dashboard/summary");
export const getChildren = (search = "") =>
  apiRequest(`/children${search ? `?search=${encodeURIComponent(search)}` : ""}`);
export const registerChild = (child) =>
  apiRequest("/children", { method: "POST", body: JSON.stringify(child) });
export const getAncPregnancies = (motherId) =>
  apiRequest(`/anc/pregnancies?motherId=${encodeURIComponent(motherId)}`);
export const createPregnancy = (pregnancy) =>
  apiRequest("/anc/pregnancies", { method: "POST", body: JSON.stringify(pregnancy) });
export const getAncVisits = (pregnancyId) =>
  apiRequest(`/anc/pregnancies/${encodeURIComponent(pregnancyId)}/visits`);
export const createAncVisit = (pregnancyId, visit) =>
  apiRequest(`/anc/pregnancies/${encodeURIComponent(pregnancyId)}/visits`, {
    method: "POST",
    body: JSON.stringify(visit),
  });

export const registerMother = (mother) =>
  apiRequest("/mothers", {
    method: "POST",
    body: JSON.stringify(mother),
  });

export const getMotherQr = (id) =>
  apiRequest(`/mothers/${encodeURIComponent(id)}/qr`);
export const getSettingsProfile = () => apiRequest("/settings/profile");
export const saveSettingsProfile = (profile) =>
  apiRequest("/settings/profile", { method: "PUT", body: JSON.stringify(profile) });
export const getSettingsPreferences = () => apiRequest("/settings/preferences");
export const saveSettingsPreferences = (preferences) =>
  apiRequest("/settings/preferences", { method: "PUT", body: JSON.stringify(preferences) });
export const getFacilitySettings = () => apiRequest("/settings/facility");
export const saveFacilitySettings = (facility) =>
  apiRequest("/settings/facility", { method: "PUT", body: JSON.stringify(facility) });
export const getHealthRecords = (subject) =>
  apiRequest(`/health-records?${subject.type === "mother" ? "motherId" : "childId"}=${encodeURIComponent(subject.id)}`);
export const createHealthRecord = (record) =>
  apiRequest("/health-records", { method: "POST", body: JSON.stringify(record) });
export const getAppointments = (from, to) =>
  apiRequest(`/appointments?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`);
export const createAppointment = (appointment) =>
  apiRequest("/appointments", { method: "POST", body: JSON.stringify(appointment) });
export const getImmunizations = (childId) =>
  apiRequest(`/immunizations?childId=${encodeURIComponent(childId)}`);
export const createImmunization = (record) =>
  apiRequest("/immunizations", { method: "POST", body: JSON.stringify(record) });
export const getReportSummary = () => apiRequest("/reports/summary");
