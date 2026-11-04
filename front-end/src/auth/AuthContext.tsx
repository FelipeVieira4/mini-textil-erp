import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import {
  api,
  empresaStorage,
  setUnauthorizedHandler,
  tokenStorage,
  tokenValido,
} from '../api/http';

export interface Operador {
  id: number;
  nome: string;
  loginName: string;
  empresaId: number | null;
}

interface LoginResponse {
  token: string;
  expiresInSeconds: number;
  operador: Operador;
}

interface AuthContextValue {
  operador: Operador | null;
  autenticado: boolean;
  carregando: boolean;
  /** Equivalente ao UsuarioSessao.getEmpresaAtiva(): a escolhida ou, se não houver, a do operador. */
  empresaAtivaId: number | null;
  login: (loginName: string, senha: string) => Promise<void>;
  logout: () => void;
  setEmpresaAtiva: (id: number) => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [operador, setOperador] = useState<Operador | null>(null);
  const [empresaEscolhida, setEmpresaEscolhida] = useState<number | null>(() => empresaStorage.get());
  // começa "carregando" só se existir um token que vale a pena validar
  const [carregando, setCarregando] = useState(() => tokenValido(tokenStorage.get()));

  const logout = useCallback(() => {
    tokenStorage.clear();
    empresaStorage.clear();
    setOperador(null);
    setEmpresaEscolhida(null);
  }, []);

  // Ao abrir/recarregar a página: valida o token guardado e recupera o operador
  useEffect(() => {
    if (!tokenValido(tokenStorage.get())) {
      tokenStorage.clear();
      return;
    }
    let ativo = true;
    api<Operador>('/api/auth/me')
      .then((op) => ativo && setOperador(op))
      .catch(() => tokenStorage.clear())
      .finally(() => ativo && setCarregando(false));
    return () => {
      ativo = false;
    };
  }, []);

  // 401 em qualquer chamada autenticada => volta para o login
  useEffect(() => {
    setUnauthorizedHandler(logout);
    return () => setUnauthorizedHandler(null);
  }, [logout]);

  const login = useCallback(async (loginName: string, senha: string) => {
    const resposta = await api<LoginResponse>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ loginName, senha }),
    });
    tokenStorage.set(resposta.token);
    setOperador(resposta.operador);
  }, []);

  const setEmpresaAtiva = useCallback((id: number) => {
    empresaStorage.set(id);
    setEmpresaEscolhida(id);
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({
      operador,
      autenticado: operador !== null,
      carregando,
      empresaAtivaId: empresaEscolhida ?? operador?.empresaId ?? null,
      login,
      logout,
      setEmpresaAtiva,
    }),
    [operador, carregando, empresaEscolhida, login, logout, setEmpresaAtiva],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth deve ser usado dentro de <AuthProvider>');
  return ctx;
}
