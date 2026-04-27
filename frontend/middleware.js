import { NextResponse } from 'next/server';

/**
 * Decodifica el payload de un JWT sin verificar la firma.
 * La verificación de firma se realiza en el backend.
 * Aquí solo comprobamos si el token existe y no ha expirado.
 */
function decodeJwtPayload(token) {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const json = Buffer.from(base64, 'base64').toString('utf8');
    return JSON.parse(json);
  } catch {
    return null;
  }
}

function isTokenValid(token) {
  if (!token) return false;
  const payload = decodeJwtPayload(token);
  if (!payload) return false;
  // Comprobar expiración (exp está en segundos Unix)
  if (payload.exp && Date.now() / 1000 > payload.exp) return false;
  return true;
}

export function middleware(request) {
  const pathname = request.nextUrl.pathname;

  const isPublicRoute =
    pathname === '/login' ||
    pathname === '/registro' ||
    pathname === '/';

  if (isPublicRoute) {
    return NextResponse.next();
  }

  if (pathname.startsWith('/dashboard')) {
    const token = request.cookies.get('accessToken')?.value;

    if (!isTokenValid(token)) {
      const loginUrl = new URL('/login', request.url);
      return NextResponse.redirect(loginUrl);
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: [
    '/dashboard/:path*',
    '/login',
    '/registro',
  ],
};