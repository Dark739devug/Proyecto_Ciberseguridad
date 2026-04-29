'use client';

import React, { useState, useEffect, useRef } from 'react';
import { toast } from 'react-toastify';
import styles from './crear-factura-modal.module.css';
import ENDPOINTS from '../../services/api';


function normalizeToken(raw) {
    if (!raw) return null;
    let t = String(raw);
    if ((t.startsWith('"') && t.endsWith('"')) || (t.startsWith("'") && t.endsWith("'"))) {
        t = t.slice(1, -1);
    }
    try {
        const parsed = JSON.parse(t);
        return parsed.token || parsed.accessToken || parsed;
    } catch {}
    return t;
}

function getUserIdFromStorage() {
    try {
        const stored = localStorage.getItem('accessToken') || localStorage.getItem('token');
        if (!stored) return null;
        const parsed = JSON.parse(stored);
        return parsed.idUsuario || null;
    } catch {
        return null;
    }
}



export default function CrearFacturaModal({ isOpen, onClose, onSuccess }) {

    const [loading, setLoading] = useState(false);
    const isSubmittingRef = useRef(false);

    const [productos, setProductos] = useState([]);
    const [formData, setFormData] = useState({
        idTipoDocumento: '',
        observaciones: '',
        detalles: []
    });

    useEffect(() => {
        if (isOpen) {
            cargarProductos();
        }
    }, [isOpen]);

    async function cargarProductos() {
        try {
            const token = normalizeToken(localStorage.getItem('accessToken'));
            const headers = token ? { Authorization: `Bearer ${token}` } : {};

            const res = await fetch(ENDPOINTS.productos, { headers });

            if (res.ok) {
                const data = await res.json();
                setProductos(data);
            }
        } catch (e) {
            console.error(e);
        }
    }

    
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (isSubmittingRef.current) return;

        setLoading(true);
        isSubmittingRef.current = true;

        try {
            const token = normalizeToken(localStorage.getItem('accessToken'));

            const headers = {
                'Content-Type': 'application/json',
                Accept: 'application/json'
            };

            if (token) headers.Authorization = `Bearer ${token}`;

            const payload = {
                ...formData,
                numeroFactura: Date.now().toString()
            };

            const res = await fetch(ENDPOINTS.facturas, {
                method: 'POST',
                headers,
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                throw new Error(`Error ${res.status}`);
            }

            const data = await res.json();

            toast.success('Factura creada correctamente');

            if (onSuccess) onSuccess(data);
            onClose();

        } catch (error) {
            console.error(error);
            toast.error('Error al crear factura');
        } finally {
            setLoading(false);
            isSubmittingRef.current = false;
        }
    };

    if (!isOpen) return null;

    return (
        <div className={styles.overlay}>
            <div className={styles.modal}>
                <h2>Crear Factura</h2>

                <form onSubmit={handleSubmit}>

                    <div>
                        <label>Tipo Documento</label>
                        <input
                            value={formData.idTipoDocumento}
                            onChange={(e) =>
                                setFormData({ ...formData, idTipoDocumento: e.target.value })
                            }
                        />
                    </div>

                    <div>
                        <label>Observaciones</label>
                        <textarea
                            value={formData.observaciones}
                            onChange={(e) =>
                                setFormData({ ...formData, observaciones: e.target.value })
                            }
                        />
                    </div>

                    <button type="submit" disabled={loading}>
                        {loading ? 'Guardando...' : 'Crear Factura'}
                    </button>
                </form>

            </div>
        </div>
    );
}