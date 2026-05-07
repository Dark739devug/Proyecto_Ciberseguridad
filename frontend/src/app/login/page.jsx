'use client';
import React, { useState, useEffect } from 'react'; 
import { useRouter } from 'next/navigation';
import styles from './login.module.css';
import { ToastContainer, toast } from 'react-toastify';
import ENDPOINTS from '../services/api'; 

export default function Login() {
  const router = useRouter();
  const [showPassword, setShowPassword] = useState(false);
 

  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('accessToken');
      if (token) {
        router.replace('/dashboard');
        return;
      }


    }
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSuccessfulLogin = (nombre) => {
    toast.success(`¡Bienvenido ${nombre}!`, { position: 'top-center' });
    router.push('/dashboard');
  };

  const setCookie = (token) => {
    const isSecure = window.location.protocol === 'https:';
    const secureFlag = isSecure ? '; Secure' : '';
    document.cookie = `accessToken=${token}; path=/; max-age=${60 * 60 * 24 * 7}; SameSite=Lax${secureFlag}`;

  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const response = await fetch(ENDPOINTS.login, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: formData.email, password: formData.password }),
      });

      const raw = await response.text();
      let token = null;
      let parsed = {};
      let nombre = '';

      if (response.ok && raw) {
        try {
          parsed = JSON.parse(raw);
          token = parsed.token || parsed.accessToken || parsed.jwt || parsed.access_token || null;
          nombre = parsed.nombre || parsed.name || '';
          const email = parsed.email || formData.email || '';
          const rol = parsed.rol || parsed.role || '';
          
          if (token) {
            localStorage.setItem('accessToken', token);
            localStorage.setItem('token', token);
            localStorage.setItem('nombre', nombre);
            localStorage.setItem('email', email);
            localStorage.setItem('rol', rol);


            setCookie(token);
            handleSuccessfulLogin(nombre);

            return;
          }
        } catch (e) {
          token = raw.trim();
          if (token) {
            localStorage.setItem('accessToken', token);

            setCookie(token);
            handleSuccessfulLogin('Usuario');
            return;
          }
        }
      } 
      
      let serverMsg = 'Credenciales incorrectas. Por favor, verifica tu correo y contraseña.';
      try {
        const parsedError = JSON.parse(raw || '{}');
        serverMsg = parsedError.message || parsedError.error || serverMsg;
      } catch (e) {}
      
      toast.error(serverMsg, { position: 'top-center' });
      
    } catch (error) {
      console.error('Error al iniciar sesión principal:', error);
      toast.error('Ocurrió un error de conexión. Intenta nuevamente.', { position: 'top-center' });
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className={styles.loginContainer}>
      <div className={styles.logoContainer}>
        <img src="/factura.png" alt="Logo" className={styles.logo} />
      </div>

      <form onSubmit={handleSubmit} className={styles.formContainer}>

        <div className={styles.inputContainer}> 
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
            <path d="M12 2a5 5 0 1 1 -5 5l.005 -.217a5 5 0 0 1 4.995 -4.783z" />
            <path d="M14 14a5 5 0 0 1 5 5v1a2 2 0 0 1 -2 2h-10a2 2 0 0 1 -2 -2v-1a5 5 0 0 1 5 -5h4z" />
          </svg>
          <input
            type="email"
            name="email"
            placeholder="Correo Electrónico"
            value={formData.email}
            onChange={handleChange}
            required
            className={styles.input}
            disabled={loading}
          />
        </div>

        <div className={styles.inputContainer}>
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
            <path d="M12 2a5 5 0 0 1 5 5v3a3 3 0 0 1 3 3v6a3 3 0 0 1 -3 3h-10a3 3 0 0 1 -3 -3v-6a3 3 0 0 1 3 -3v-3a5 5 0 0 1 5 -5m0 12a2 2 0 0 0 -1.995 1.85l-.005 .15a2 2 0 1 0 2 -2m0 -10a3 3 0 0 0 -3 3v3h6v-3a3 3 0 0 0 -3 -3" />
          </svg>
          <input
            type={showPassword ? 'text' : 'password'}
            name="password"
            placeholder="Contraseña"
            value={formData.password}
            onChange={handleChange}
            required
            className={styles.input}
            disabled={loading}
          />
          <button
            type="button"
            className={styles.eyeButton}
            onClick={() => setShowPassword(!showPassword)}
            aria-pressed={showPassword}
            aria-label={showPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'}
          >
            {showPassword ? (
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="currentColor">
                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                <path d="M12 4c4.29 0 7.863 2.429 10.665 7.154l.22 .379l.045 .1l.03 .083l.014 .055l.014 .082l.011 .1v.11l-.014 .111a.992 .992 0 0 1 -.026 .11l-.039 .108l-.036 .075l-.016 .03c-2.764 4.836 -6.3 7.38 -10.555 7.499l-.313 .004c-4.396 0 -8.037 -2.549 -10.868 -7.504a1 1 0 0 1 0 -.992c2.831 -4.955 6.472 -7.504 10.868 -7.504zm0 5a3 3 0 1 0 0 6a3 3 0 0 0 0 -6z" />
              </svg>
            ) : (
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                <path d="M21 9c-2.4 2.667 -5.4 4 -9 4c-3.6 0 -6.6 -1.333 -9 -4" />
                <path d="M3 15l2.5 -3.8" />
                <path d="M21 14.976l-2.492 -3.776" />
                <path d="M9 17l.5 -4" />
                <path d="M15 17l-.5 -4" />
              </svg>
            )}
          </button>
        </div>

        <button type="submit" className={styles.button} disabled={loading}>
          {loading ? 'Iniciando sesión...' : 'Iniciar sesión'}
        </button>

      


        
      </form>
      
      <ToastContainer position="top-center" />
    </div>
  );
}