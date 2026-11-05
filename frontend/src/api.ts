const apiUrl = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api';
export type Profile = { id: string; email: string; fullName: string; role: string; tenantId: string; tenantSlug: string; tenantName: string };
export type Member = { id: string; email: string; fullName: string; role: string; createdAt: string };
export type Organization = { id: string; slug: string; displayName: string; createdAt: string };
export function api<T>(path: string, credentials: string, init: RequestInit = {}): Promise<T> {
  return fetch(`${apiUrl}${path}`, { ...init, headers: { 'Content-Type': 'application/json', Authorization: `Basic ${credentials}`, ...init.headers } }).then(async response => {
    if (!response.ok) throw new Error(response.status === 401 ? 'Email or password did not match.' : `Request failed (${response.status}).`);
    if (response.status === 204) return undefined as T;
    return response.json() as Promise<T>;
  });
}
