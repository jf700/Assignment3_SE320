/** @type {import('next').NextConfig} */
const nextConfig = {
  output: process.env.NEXT_OUTPUT === 'export' ? 'export' : undefined,
  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    unoptimized: true,
  },
  async rewrites() {
    if (process.env.NEXT_OUTPUT === 'export') return []
    return [
      { source: '/auth/:path*', destination: 'http://localhost:8080/auth/:path*' },
      { source: '/sessions/:path*', destination: 'http://localhost:8080/sessions/:path*' },
      { source: '/diary/:path*', destination: 'http://localhost:8080/diary/:path*' },
      { source: '/progress/:path*', destination: 'http://localhost:8080/progress/:path*' },
      { source: '/crisis/:path*', destination: 'http://localhost:8080/crisis/:path*' },
      { source: '/actuator/:path*', destination: 'http://localhost:8080/actuator/:path*' },
    ]
  },
}

export default nextConfig
