// app/dashboard/layout.jsx
import React from 'react';
import DashboardClientLayout from '../components/DashboardClientLayout';

export const metadata = {
  title: 'Dashboard',
};


export default function DashboardLayout({ children }) {
  return <DashboardClientLayout>{children}</DashboardClientLayout>;
}