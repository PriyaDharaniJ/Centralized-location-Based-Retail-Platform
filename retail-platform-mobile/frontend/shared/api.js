const API_BASE = 'https://centralized-location-based-retail.onrender.com/api';
const api = {
    token: () => localStorage.getItem('rp_token'),
    getUser: () => JSON.parse(localStorage.getItem('rp_user') || 'null'),
    headers(extra = {}) {
        const h = { 'Content-Type': 'application/json', ...extra };
        const t = this.token();
        if (t) h['Authorization'] = 'Bearer ' + t;
        return h;
    },
    async get(path) {
        const r = await fetch(API_BASE + path, { headers: this.headers() });
        return r.json();
    },
    async post(path, data) {
        const r = await fetch(API_BASE + path, {
            method: 'POST', headers: this.headers(), body: JSON.stringify(data)
        });
        return r.json();
    },
    async put(path, data) {
        const r = await fetch(API_BASE + path, {
            method: 'PUT', headers: this.headers(), body: JSON.stringify(data)
        });
        return r.json();
    },
    async patch(path, data) {
        const r = await fetch(API_BASE + path, {
            method: 'PATCH', headers: this.headers(), body: JSON.stringify(data)
        });
        return r.json();
    },
    async delete(path) {
        const r = await fetch(API_BASE + path, { method: 'DELETE', headers: this.headers() });
        return r.json();
    },
    logout() {
        localStorage.removeItem('rp_token');
        localStorage.removeItem('rp_user');
    }
};
