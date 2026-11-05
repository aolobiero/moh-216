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
    throw new Error(`API request failed: HTTP ${response.status}`);
  }

  const contentType = response.headers.get("content-type") || "";

  if (contentType.includes("application/json")) {
    return response.json();
  }

  return response.blob();
}

export const getMothers = () => apiRequest("/mothers");

export const registerMother = (mother) =>
  apiRequest("/mothers", {
    method: "POST",
    body: JSON.stringify(mother),
  });

export const getMotherQr = (id) =>
  apiRequest(`/mothers/${encodeURIComponent(id)}/qr`);
