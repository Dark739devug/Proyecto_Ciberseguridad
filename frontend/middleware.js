
import { NextResponse } from 'next/server';

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
  if (payload.exp && Date.now() / 1000 > payload.exp) return false;
  return true;
}

export function middleware(request) {
  const pathname = request.nextUrl.pathname;

  if (pathname.startsWith('/dashboard')) {
    
    const token = request.cookies.get('accessToken')?.value;

    if (!isTokenValid(token)) {
      
      const response = NextResponse.redirect(new URL('/login', request.url));
      response.cookies.set('accessToken', '', { maxAge: 0, path: '/' });
      return response;
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/dashboard/:path*'],
};