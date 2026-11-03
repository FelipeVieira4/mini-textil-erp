import { useState, type ReactNode } from 'react';
import {
  Box,
  Button,
  Collapse,
  FormControl,
  IconButton,
  InputLabel,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  MenuItem,
  Select,
  Toolbar,
  Typography,
  type SelectChangeEvent,
} from '@mui/material';
import { useTheme } from '@mui/material/styles';
import MenuIcon from '@mui/icons-material/Menu';
import DescriptionOutlinedIcon from '@mui/icons-material/DescriptionOutlined';
import FolderOpenOutlinedIcon from '@mui/icons-material/FolderOpenOutlined';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import LogoutIcon from '@mui/icons-material/Logout';
import { menuItems, type NavItem } from './menuItems';

const DRAWER_WIDTH = 300;

// Dados fictícios, só para a tela ter algo no seletor.
const empresasMock = [
  { id: 1, nome: 'Têxtil Norte Ltda' },
  { id: 2, nome: 'Malharia Sul S.A.' },
];

interface AppLayoutProps {
  children?: ReactNode;
}

export default function AppLayout({ children }: AppLayoutProps) {
  const { palette } = useTheme();
  const s = palette.sidebar;

  const [menuAberto, setMenuAberto] = useState(true);
  const [gruposAbertos, setGruposAbertos] = useState<Record<string, boolean>>({
    Empresa: true,
    Operadores: true,
    Itens: true,
  });
  const [selecionado, setSelecionado] = useState('/');
  const [empresa, setEmpresa] = useState('');

  const alternarGrupo = (label: string) =>
    setGruposAbertos((prev) => ({ ...prev, [label]: !prev[label] }));

  const itemSx = (ativo: boolean, nivel: number) => ({
    color: s.text,
    py: 1,
    pl: 3 + nivel * 3,
    pr: 2,
    borderRadius: 0,
    bgcolor: ativo ? s.selected : 'transparent',
    '&:hover': { bgcolor: ativo ? s.selected : s.hover },
  });

  const renderItem = (item: NavItem, nivel = 0) => {
    if (item.children) {
      const aberto = !!gruposAbertos[item.label];
      return (
        <Box key={item.label}>
          <ListItemButton onClick={() => alternarGrupo(item.label)} sx={itemSx(false, nivel)}>
            <ListItemIcon sx={{ color: s.muted, minWidth: 36 }}>
              <FolderOpenOutlinedIcon />
            </ListItemIcon>
            <ListItemText primary={item.label} slotProps={{ primary: { fontWeight: 500 } }} />
            <ExpandMoreIcon
              fontSize="small"
              sx={{
                color: s.muted,
                transform: aberto ? 'rotate(0deg)' : 'rotate(-90deg)',
                transition: 'transform 150ms',
              }}
            />
          </ListItemButton>
          <Collapse in={aberto} timeout="auto" unmountOnExit>
            <List disablePadding>{item.children.map((c) => renderItem(c, nivel + 1))}</List>
          </Collapse>
        </Box>
      );
    }

    return (
      <ListItemButton
        key={item.label}
        selected={selecionado === item.path}
        onClick={() => setSelecionado(item.path ?? '')}
        sx={itemSx(selecionado === item.path, nivel)}
      >
        <ListItemIcon sx={{ color: s.muted, minWidth: 36 }}>
          <DescriptionOutlinedIcon />
        </ListItemIcon>
        <ListItemText primary={item.label} slotProps={{ primary: { fontWeight: 500 } }} />
      </ListItemButton>
    );
  };

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* Menu lateral */}
      <Box
        component="nav"
        sx={{
          width: menuAberto ? DRAWER_WIDTH : 0,
          flexShrink: 0,
          overflow: 'hidden',
          transition: 'width 200ms ease',
        }}
      >
        <Box
          sx={{
            width: DRAWER_WIDTH,
            height: '100vh',
            position: 'sticky',
            top: 0,
            display: 'flex',
            flexDirection: 'column',
            bgcolor: s.background,
            color: s.text,
          }}
        >
          <Box sx={{ py: 2.5, textAlign: 'center', borderBottom: `1px solid ${s.border}` }}>
            <Typography variant="h6" fontWeight={600}>
              MiniTextil ERP
            </Typography>
          </Box>

          <List sx={{ flexGrow: 1, overflowY: 'auto', py: 2 }}>
            {menuItems.map((item) => renderItem(item))}
          </List>

          <Box sx={{ borderTop: `1px solid ${s.border}`, py: 2.5, textAlign: 'center' }}>
            <Typography variant="body2" sx={{ color: s.muted }}>
              User ADM
            </Typography>
            <Button
              startIcon={<LogoutIcon />}
              sx={{ mt: 1.5, color: s.danger, fontWeight: 600, textTransform: 'none' }}
              onClick={() => console.log('sair')}
            >
              Sair
            </Button>
          </Box>
        </Box>
      </Box>

      {/* Cabeçalho + conteúdo */}
      <Box sx={{ flexGrow: 1, minWidth: 0, display: 'flex', flexDirection: 'column' }}>
        <Toolbar
          sx={{
            bgcolor: 'background.paper',
            borderBottom: 1,
            borderColor: 'divider',
            minHeight: 110,
            gap: 2,
          }}
        >
          <IconButton color="primary" onClick={() => setMenuAberto((v) => !v)} aria-label="Alternar menu">
            <MenuIcon />
          </IconButton>

          {/* Espaço para o título da página */}
          <Box sx={{ flexGrow: 1 }} />

          <FormControl size="small" sx={{ minWidth: 200 }}>
            <InputLabel id="empresa-label">Empresa</InputLabel>
            <Select
              labelId="empresa-label"
              label="Empresa"
              value={empresa}
              onChange={(e: SelectChangeEvent) => setEmpresa(e.target.value)}
            >
              {empresasMock.map((e) => (
                <MenuItem key={e.id} value={String(e.id)}>
                  {e.nome}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Toolbar>

        <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
          {children}
        </Box>
      </Box>
    </Box>
  );
}
