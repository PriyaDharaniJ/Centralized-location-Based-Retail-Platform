#!/bin/bash
echo "=================================="
echo "RetailHub - Starting Services"
echo "=================================="

# Check PostgreSQL
if ! pg_isready -q 2>/dev/null; then
  echo "⚠️  PostgreSQL is not running. Please start it first."
  echo "   Ubuntu/Debian: sudo service postgresql start"
  echo "   macOS: brew services start postgresql"
fi

echo ""
echo "📋 Step 1: Setup Database"
echo "   psql -U postgres -c \"CREATE DATABASE retailplatform;\""
echo "   psql -U postgres -d retailplatform -f database/schema.sql"
echo ""
echo "🚀 Step 2: Start Backend"
echo "   cd backend && mvn spring-boot:run"
echo ""
echo "🌐 Step 3: Open Frontend"
echo "   User:     frontend/user/index.html"
echo "   Retailer: frontend/retailer/index.html"
echo "   Admin:    frontend/admin/index.html"
echo ""
echo "🔑 Default Credentials:"
echo "   Admin: admin@retailplatform.com / admin123"
echo "   Demo Retailer: rajan@demo.com / admin123"
echo "   Demo User: user@demo.com / admin123"
echo "=================================="
