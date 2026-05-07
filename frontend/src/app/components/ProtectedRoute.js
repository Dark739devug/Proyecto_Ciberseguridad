'use client';

import { useEffect, useState } from 'react';
import { useRouter, usePathname } from 'next/navigation';

export default function ProtectedRoute({ children }) {
  const router = useRouter();
  const pathname = usePathname();
  const [authorized, setAuthorized] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');

    if (!token && pathname.startsWith('/dashboard')) {
      router.replace('/login');
      return;
    }

    if (token && (pathname === '/login' || pathname === '/registro')) {
      router.replace('/dashboard');
      return;
    }

    setAuthorized(true);
  }, [router, pathname]);

  if (!authorized) return null;

  return <>{children}</>;
}