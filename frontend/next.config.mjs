const nextConfig = {
  poweredByHeader: false,

  experimental: {
    workerThreads: false,
    cpus: 1,
  },

  async headers() {
    return [
      {
        source: '/(.*)',
        headers: [
          {
            key: 'Content-Security-Policy',
            value: [
              "default-src 'self'",
              
              
              "script-src 'self' 'unsafe-eval' 'unsafe-inline'",

              // estilos (necesario para Next.js + CSS modules)
              "style-src 'self' 'unsafe-inline'",

              // imágenes
              "img-src 'self' data: blob:",

              // fuentes
              "font-src 'self' data:",

           
              "connect-src 'self' http://localhost:* https://*",

              "object-src 'none'",
              "frame-src 'none'",

              
              "worker-src 'self' blob:",

              "manifest-src 'self'",
              "frame-ancestors 'none'",
            ].join('; ')
          },

          {
            key: 'X-Frame-Options',
            value: 'DENY'
          },

          {
            key: 'X-Content-Type-Options',
            value: 'nosniff'
          },

          {
            key: 'Strict-Transport-Security',
            value: 'max-age=31536000; includeSubDomains'
          },

          {
            key: 'Referrer-Policy',
            value: 'strict-origin-when-cross-origin'
          },

          {
            key: 'Permissions-Policy',
            value: 'camera=(), microphone=(), geolocation=()'
          }
        ],
      },
    ];
  },
};

export default nextConfig;