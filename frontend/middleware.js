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
  // Comprobar expiración (exp está en segundos Unix)
  if (payload.exp && Date.now() / 1000 > payload.exp) return false;
  return true;
}

export function middleware(request) {
  const pathname = request.nextUrl.pathname;


  const isPublicRoute = pathname === '/login' || 
                        pathname === '/registro' || 
                        pathname === '/';


  if (isPublicRoute) {
    return NextResponse.next();
  }

 
  if (pathname.startsWith('/dashboard')) {
    return NextResponse.next();
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/dashboard/:path*'],
};