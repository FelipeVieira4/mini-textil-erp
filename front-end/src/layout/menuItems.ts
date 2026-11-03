export interface NavItem {
  label: string;
  path?: string;
  children?: NavItem[];
}

export const menuItems: NavItem[] = [
  { label: 'Home', path: '/' },
  {
    label: 'Empresa',
    children: [{ label: 'Cadastro de Empresa', path: '/empresa/cadastro' }],
  },
  {
    label: 'Operadores',
    children: [
      { label: 'Cadastro de Operador', path: '/operadores/cadastro' },
      { label: 'Consulta de Operador', path: '/operadores/consulta' },
    ],
  },
  {
    label: 'Itens',
    children: [
      { label: 'Cadastro de Item', path: '/itens/cadastro' },
      { label: 'Consulta de Itens', path: '/itens/consulta' },
      { label: 'Controle de Variante', path: '/itens/variantes' },
      { label: 'Controle de Itens Sku', path: '/itens/sku' },
    ],
  },
];
