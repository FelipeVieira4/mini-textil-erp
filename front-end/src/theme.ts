import { createTheme } from '@mui/material/styles';
import { ptBR } from '@mui/material/locale';

export const theme = createTheme(
  {
    palette: {
      primary: { main: '#4f46e5' },
      background: { default: '#f4f5fa' },
      // cor própria usada no menu lateral
      sidebar: {
        background: '#111827',
        text: '#e5e7eb',
        muted: '#9ca3af',
        hover: 'rgba(255,255,255,0.06)',
        selected: 'rgba(255,255,255,0.12)',
        danger: '#f87171',
        border: 'rgba(255,255,255,0.08)',
      },
    },
    typography: {
      fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    },
    shape: { borderRadius: 8 },
  },
  ptBR,
);

declare module '@mui/material/styles' {
  interface Palette {
    sidebar: {
      background: string;
      text: string;
      muted: string;
      hover: string;
      selected: string;
      danger: string;
      border: string;
    };
  }
  interface PaletteOptions {
    sidebar?: Palette['sidebar'];
  }
}
