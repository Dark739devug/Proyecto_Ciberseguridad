'use client';

import React from 'react';
import Sidebar from './Sidebar';
import ProtectedRoute from './ProtectedRoute';
import styles from '../app/dashboard/dashboard.module.css';


export default function DashboardClientLayout({ children }) {
  return (
    <ProtectedRoute>
      <div className={styles.layout}>
        <Sidebar />
        <main className={styles.content}>
          {children}
        </main>
      </div>
    </ProtectedRoute>
  );
}