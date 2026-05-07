'use client';
import React, { useState, useEffect } from 'react'; // 👈 IMPORTANTE: Añadir useEffect
import { useRouter } from 'next/navigation';
import styles from './login.module.css';
import apiCerti from '../services/API_Certi_API';

export default function CertiLoginModal({
    isOpen,
    onClose,
    onSuccess,
    initialEmail = ''
}) {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    // Reset o precarga cuando abre el modal
    useEffect(() => {
        if (isOpen) {
            setFormData({
                username: initialEmail || '',
                password: ''
            });
            setShowPassword(false);
        }
    }, [isOpen, initialEmail]);
>>>>>>> Stashed changes

      const raw = await response.text();
      let token = null;
      let parsed = {};
      let nombre = '';

      if (response.ok && raw) {
        try {
          // Intenta parsear JSON
          parsed = JSON.parse(raw);
          token = parsed.token || parsed.accessToken || parsed.jwt || parsed.access_token || null;
          nombre = parsed.nombre || parsed.name || '';
          const email = parsed.email || formData.email || '';
          const rol = parsed.rol || parsed.role || '';
          
          if (token) {
            // Guardar datos del login principal
            localStorage.setItem('accessToken', token);
            localStorage.setItem('token', token);
            localStorage.setItem('nombre', nombre);
            localStorage.setItem('email', email);
            localStorage.setItem('rol', rol);
            setUserEmail(email); // Guardar email para el modal


    const handleCertiLogin = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const payload = {
                username: formData.username,
                password: formData.password
            };

            const response = await apiCerti.post('/auth/login', payload);

            const parsed = response.data;
            const token =
                parsed.token ||
                parsed.accessToken ||
                parsed.jwt ||
                parsed.access_token;

            if (!token) {
                throw new Error("No se recibió el token de certificación.");
            }

            localStorage.setItem('certiToken', token);
            localStorage.setItem('certiUserEmail', formData.username);

            toast.success("Credenciales guardadas correctamente", {
                position: 'top-center'
            });

            onSuccess(token);

        } catch (error) {
            const responseData = error.response?.data || {};

            let errorMsg =
                responseData.message ||
                responseData.error ||
                'Credenciales incorrectas. Verifica usuario y contraseña.';

            if (error.message.includes('401')) {
                errorMsg = 'Error 401: Credenciales inválidas.';
            }

            toast.error(errorMsg, { position: 'top-center' });
            console.error('Error login certificador:', error.response || error);

        } finally {
            setLoading(false);
>>>>>>> Stashed changes
        }
      } 
      
      // Si el login principal falla (4xx, 5xx), muestra la alerta
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
      setLoading(false); // Finalizar carga
    }
  };

  /**
   * Lógica tras el éxito del modal secundario
   */
  const handleCertiSuccess = () => {
    setShowCertiModal(false);
    const nombre = localStorage.getItem('nombre') || 'Usuario';
    handleSuccessfulLogin(nombre); // Redirigir al dashboard
  };
  
  /**
   * Lógica si se cierra o salta el modal secundario
   */
  const handleCertiClose = () => {
    setShowCertiModal(false);
    const nombre = localStorage.getItem('nombre') || 'Usuario';
    // Se redirige igual, pero el usuario no tendrá el token de certificación
    handleSuccessfulLogin(nombre); 
  };


    return (
        <div className={styles.overlay}>
            <div className={styles.modal} onClick={(e) => e.stopPropagation()}>

                <button
                    className={styles.modalCloseButton}
                    onClick={onClose}
                    disabled={loading}
                >
                    &times;
                </button>

                <div className={styles.header}>
                    <h2 className={styles.formTitle}>Acceso Certificador</h2>
                    <p className={styles.formSubtitle}>
                        Ingresa usuario y contraseña para obtener token
                    </p>
                </div>

                <form
                    onSubmit={handleCertiLogin}
                    className={styles.formContainer}
                >

                    <div className={styles.inputContainer}>
                        <input
                            type="text"
                            name="username"
                            placeholder="Usuario"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            disabled={loading}
                            className={styles.input}
                        />
                    </div>

                    <div className={styles.inputContainer}>
                        <input
                            type={showPassword ? 'text' : 'password'}
                            name="password"
                            placeholder="Contraseña"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            disabled={loading}
                            className={styles.input}
                        />

                        <button
                            type="button"
                            onClick={() => setShowPassword(!showPassword)}
                            disabled={loading}
                        >
                            {showPassword ? '🙈' : '👁️'}
                        </button>
                    </div>

                    <button type="submit" disabled={loading}>
                        {loading ? 'Obteniendo Token...' : 'Obtener Token'}
                    </button>

                </form>
            </div>
>>>>>>> Stashed changes
        </div>

        <div className={styles.inputContainer}>
          <svg xmlns="http://www.w3.org/2000/svg" 
            width="24" 
            height="24" 
            viewBox="0 0 24 24" 
            fill="currentColor" 
          >
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
              <svg xmlns="http://www.w3.org/2000/svg" 
                width="28" 
                height="28" 
                viewBox="0 0 24 24" 
                fill="currentColor" 
              >
                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                <path d="M12 4c4.29 0 7.863 2.429 10.665 7.154l.22 .379l.045 .1l.03 .083l.014 .055l.014 .082l.011 .1v.11l-.014 .111a.992 .992 0 0 1 -.026 .11l-.039 .108l-.036 .075l-.016 .03c-2.764 4.836 -6.3 7.38 -10.555 7.499l-.313 .004c-4.396 0 -8.037 -2.549 -10.868 -7.504a1 1 0 0 1 0 -.992c2.831 -4.955 6.472 -7.504 10.868 -7.504zm0 5a3 3 0 1 0 0 6a3 3 0 0 0 0 -6z" />
              </svg>
            ) : (
              <svg xmlns="http://www.w3.org/2000/svg" 
                width="28" 
                height="28"
                viewBox="0 0 24 24" 
                fill="none" 
                stroke="currentColor" 
                strokeWidth="2" 
                strokeLinecap="round" 
                strokeLinejoin="round" 
              >
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
        <div className={styles.linksContainer}>
          <a href="/registro" className={styles.link}>¿Crear cuenta?</a>
        </div>
      </form>
      
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