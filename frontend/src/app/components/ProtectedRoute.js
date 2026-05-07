'use client';


import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

export default function ProtectedRoute({ children }) {
  const router = useRouter();
  const [authorized, setAuthorized] = useState(false);

  useEffect(() => {
    const hasCookie = document.cookie.includes('accessToken');

    if (!hasCookie) {
      router.replace('/login');
    } else {
      setAuthorized(true);
    }
  }, []);

  if (!authorized) return null;

  return children;

}