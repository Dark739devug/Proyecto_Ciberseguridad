
import { NextResponse } from 'next/server';

function decodeJwtPayload(token) {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;

    const base64 = parts[1]
      .replace(/-/g, '+')
      .replace(/_/g, '/');

    return JSON.parse(Buffer.from(base64, 'base64').toString('utf8'));
  } catch {
    return null;
  }
}

function isTokenValid(token) {
  if (!token) return false;

  const payload = decodeJwtPayload(token);
  if (!payload) return false;


  if (payload.exp && Date.now() / 1000 > payload.exp) {
    return false;
  }

>>>>>>> Stashed changes
  return true;
}

export function middleware(request) {
  const token = request.cookies.get('accessToken')?.value;
  const { pathname } = request.nextUrl;

  if (pathname.startsWith('/dashboard')) {

    if (!isTokenValid(token)) {
      
      const response = NextResponse.redirect(new URL('/login', request.url));
      response.cookies.delete('accessToken');
>>>>>>> Stashed changes
      return response;
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/dashboard/:path*'],
};