import { useState, type FormEvent } from 'react';
import {
  Box,
  Button,
  FormControl,
  FormLabel,
  IconButton,
  InputAdornment,
  OutlinedInput,
  Typography,
} from '@mui/material';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import VisibilityOffOutlinedIcon from '@mui/icons-material/VisibilityOffOutlined';

// Coloque a foto em: public/login-bg.jpg
const BACKGROUND_IMAGE = '/login-bg.jpg';

function RequiredLabel({ children, htmlFor }: { children: string; htmlFor: string }) {
  return (
    <FormLabel
      htmlFor={htmlFor}
      sx={{ fontSize: 14, fontWeight: 500, color: 'text.secondary', mb: 0.5, ml: 0.5 }}
    >
      {children}{' '}
      <Box component="span" sx={{ color: 'primary.main' }} aria-hidden>
        •
      </Box>
    </FormLabel>
  );
}

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [mostrarSenha, setMostrarSenha] = useState(false);

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    // Sem integração por enquanto.
    console.log('login', { username, password });
  };

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.paper' }}>
      {/* Coluna do formulário */}
      <Box
        sx={{
          flex: { xs: 1, md: '0 0 30%' },
          minWidth: { md: 400 },
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          px: { xs: 3, md: 0 },
          alignItems: { xs: 'center', md: 'flex-start' },
          pl: { md: '132px' },
        }}
      >
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ width: '100%', maxWidth: 312, display: 'flex', flexDirection: 'column', gap: 2.5 }}
        >
          <Typography variant="h5" component="h1" fontWeight={700} color="#111827">
            Log in
          </Typography>

          <FormControl required fullWidth>
            <RequiredLabel htmlFor="username">Username</RequiredLabel>
            <OutlinedInput
              id="username"
              size="small"
              autoComplete="username"
              autoFocus
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              sx={{ bgcolor: '#f4f5fa' }}
            />
          </FormControl>

          <FormControl required fullWidth>
            <RequiredLabel htmlFor="password">Password</RequiredLabel>
            <OutlinedInput
              id="password"
              size="small"
              type={mostrarSenha ? 'text' : 'password'}
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              sx={{ bgcolor: '#f4f5fa' }}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    edge="end"
                    size="small"
                    aria-label={mostrarSenha ? 'Ocultar senha' : 'Mostrar senha'}
                    onClick={() => setMostrarSenha((v) => !v)}
                    onMouseDown={(e) => e.preventDefault()}
                  >
                    {mostrarSenha ? <VisibilityOffOutlinedIcon fontSize="small" /> : <VisibilityOutlinedIcon fontSize="small" />}
                  </IconButton>
                </InputAdornment>
              }
            />
          </FormControl>

          <Button
            type="submit"
            variant="contained"
            fullWidth
            disableElevation
            sx={{ mt: 1, py: 1, textTransform: 'none', fontWeight: 600 }}
          >
            Log in
          </Button>
        </Box>
      </Box>

      {/* Imagem lateral (escondida em telas pequenas) */}
      <Box
        sx={{
          display: { xs: 'none', md: 'block' },
          flexGrow: 1,
          bgcolor: '#cbd5e1', // cor de fallback enquanto a imagem não existe
          backgroundImage: `url(${BACKGROUND_IMAGE})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
    </Box>
  );
}
