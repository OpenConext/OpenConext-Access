import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import svgr from "vite-plugin-svgr";

// https://vite.dev/config/
export default defineConfig({
    plugins: [react(), svgr(
        {
            // svgr options: https://react-svgr.com/docs/options/
            svgrOptions: { exportType: "default", ref: true, svgo: false, titleProp: true },
            include: "**/*.svg",
        }
    )],
    server: {
        port: 3002,
        open: true,
        proxy: {
            '/myconext/api': {
                target: 'http://localhost:8081',
                changeOrigin: false,
                secure: false
            },
            '/config': {
                target: 'http://localhost:8081',
                changeOrigin: false,
                secure: false
            }
        }

    },
})
