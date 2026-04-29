'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import styles from './login.module.css';
import { ToastContainer, toast } from 'react-toastify';
import ENDPOINTS from '../services/api';

import CertiLoginModal from './CertiLoginModal';

export default function Login() {
  const router = useRouter();

  const [showPassword, setShowPassword] = useState(false);
  const [showCertiModal, setShowCertiModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [userEmail, setUserEmail] = useState('');

  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [userEmail, setUserEmail] = useState('');

  // --------------------------------------------------------------------------
  // 💡 CORRECCIÓN CLAVE: Limpiar el certiToken al cargar la página de Login.
  // Esto asegura que el modal se muestre en cada nuevo inicio de sesión,
  // incluso si el usuario no cerró la sesión del certificador manualmente.
  // --------------------------------------------------------------------------
  useEffect(() => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem("certiToken");
      localStorage.removeItem("certiUserEmail");
    }
  }, []);
  // --------------------------------------------------------------------------

  // (Opcional) limpiar tokens al entrar al login
  useEffect(() => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('certiToken');
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  /**
   * Redirección final al dashboard
   */
  const handleSuccessfulLogin = (nombre) => {
    toast.success(`¡Bienvenido ${nombre}!`, { position: 'top-center' });
    router.push('/dashboard');
  };

  /**
   * Login principal
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch(ENDPOINTS.LOGIN, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      const raw = await response.text();

      let token = null;
      let nombre = '';
      let parsed = {};

      if (response.ok && raw) {
        try {
          parsed = JSON.parse(raw);

          token =
            parsed.token ||
            parsed.accessToken ||
            parsed.jwt ||
            parsed.access_token ||
            null;

          nombre = parsed.nombre || parsed.name || '';
          const email = parsed.email || formData.email || '';
          const rol = parsed.rol || parsed.role || '';

          if (token) {
            // Guardar datos
            localStorage.setItem('accessToken', token);
            localStorage.setItem('nombre', nombre);
            localStorage.setItem('email', email);
            localStorage.setItem('rol', rol);
            // Cookie para que el middleware pueda verificar la sesión server-side
            document.cookie = `accessToken=${token}; path=/; SameSite=Strict`;
            setUserEmail(email); // Guardar email para el modal

            document.cookie = `accessToken=${token}; path=/; SameSite=Strict`;

            setUserEmail(email);

            // Login secundario
            const certiToken = localStorage.getItem('certiToken');

            if (!certiToken) {
              setShowCertiModal(true);
            } else {
              handleSuccessfulLogin(nombre);
            }

            return;
          }
        } catch (e) {
          // Si no es JSON, usar raw como token
          token = raw.trim();

          if (token) {
            localStorage.setItem('accessToken', token);
            handleSuccessfulLogin('Usuario');
            return;
          }
        }
      }

      // Error de servidor
      let serverMsg =
        'Credenciales incorrectas. Por favor, verifica tu correo y contraseña.';

      try {
        const parsedError = JSON.parse(raw || '{}');
        serverMsg = parsedError.message || serverMsg;
      } catch {}

      toast.error(serverMsg, { position: 'top-center' });
    } catch (error) {
      console.error('Error al iniciar sesión:', error);
      toast.error('Ocurrió un error de conexión. Intenta nuevamente.', {
        position: 'top-center',
      });
    } finally {
      setLoading(false);
    }
  };

  /**
   * Modal secundario éxito
   */
  const handleCertiSuccess = () => {
    setShowCertiModal(false);
    const nombre = localStorage.getItem('nombre') || 'Usuario';
    handleSuccessfulLogin(nombre);
  };

  /**
   * Modal secundario cerrar
   */
  const handleCertiClose = () => {
    setShowCertiModal(false);
    const nombre = localStorage.getItem('nombre') || 'Usuario';
    handleSuccessfulLogin(nombre);
  };

  return (
    <div className={styles.loginContainer}>
      <form onSubmit={handleSubmit} className={styles.formContainer}>
        <h2 className={styles.title}>Iniciar Sesión</h2>

        {/* EMAIL */}
        <div className={styles.inputContainer}>
          <input
            type="email"
            name="email"
            placeholder="Correo electrónico"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        {/* PASSWORD */}
        <div className={styles.inputContainer}>
          <input
            type={showPassword ? 'text' : 'password'}
            name="password"
            placeholder="Contraseña"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
          >
            {showPassword ? 'Ocultar' : 'Mostrar'}
          </button>
        </div>

        <button
          type="submit"
          className={styles.button}
          disabled={loading}
        >
          {loading ? 'Iniciando sesión...' : 'Iniciar sesión'}
        </button>

        <div className={styles.linksContainer}>
          <a href="/registro" className={styles.link}>
            ¿Crear cuenta?
          </a>
        </div>
      </form>

      {/* MODAL */}
      <CertiLoginModal
        isOpen={showCertiModal}
        onClose={handleCertiClose}
        onSuccess={handleCertiSuccess}
        initialEmail={userEmail}
      />

      <ToastContainer position="top-center" />
    </div>
  );
}