const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

const TOKEN_KEY = 'auth.token';
const EMPRESA_KEY = 'auth.empresaId';

function safeStorage() {
  try {
    return window.localStorage;
  } catch {
    return null;
  }
}

export const tokenStorage = {
  get: () => safeStorage()?.getItem(TOKEN_KEY) ?? null,
  set: (token: string) => safeStorage()?.setItem(TOKEN_KEY, token),
  clear: () => safeStorage()?.removeItem(TOKEN_KEY),
};

export const empresaStorage = {
  get: () => {
    const v = safeStorage()?.getItem(EMPRESA_KEY);
    return v ? Number(v) : null;
  },
  set: (id: number) => safeStorage()?.setItem(EMPRESA_KEY, String(id)),
  clear: () => safeStorage()?.removeItem(EMPRESA_KEY),
};

/** Lê o campo "exp" do JWT (só para saber se já expirou; quem valida de verdade é o backend). */
export function tokenValido(token: string | null): boolean {
  if (!token) return false;
  try {
    const payload = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const { exp } = JSON.parse(atob(payload)) as { exp?: number };
    return typeof exp === 'number' && exp * 1000 > Date.now();
  } catch {
    return false;
  }
}

export class ApiError extends Error {
  status: number;

  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

let onUnauthorized: (() => void) | null = null;
export function setUnauthorizedHandler(handler: (() => void) | null) {
  onUnauthorized = handler;
}

export async function api<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  if (options.body) headers.set('Content-Type', 'application/json');

  const token = tokenStorage.get();
  if (token) headers.set('Authorization', `Bearer ${token}`);

  // substitui o UsuarioSessao.empresaAtiva (que ficava na sessão do Vaadin)
  const empresaId = empresaStorage.get();
  if (empresaId) headers.set('X-Empresa-Id', String(empresaId));

  const response = await fetch(`${API_URL}${path}`, { ...options, headers });

  // token expirado/inválido numa chamada autenticada: derruba a sessão
  if (response.status === 401 && token) onUnauthorized?.();

  if (!response.ok) {
    throw new ApiError(response.status, response.statusText);
  }

  return response.status === 204 ? (undefined as T) : ((await response.json()) as T);
}
