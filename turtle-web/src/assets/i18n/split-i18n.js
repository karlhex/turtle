const fs = require('fs');
const path = require('path');

// 需要拆分的模块名（key）
const modules = [
  'contract', 'department', 'types', 'common', 'project', 'user', 'currency',
  'company', 'product', 'invoice', 'bank_account', 'reimbursement', 'guest',
  'inventory', 'contact'
];

// menu 特殊处理（在 app.menu 下）
const menuKey = 'menu';
const menuPath = ['app', 'menu'];

// 源文件和目标目录
const srcFile = path.join(__dirname, 'zh.json');
const outDir = path.join(__dirname, 'zh-CN');

if (!fs.existsSync(outDir)) fs.mkdirSync(outDir);

const zh = JSON.parse(fs.readFileSync(srcFile, 'utf8'));

// 普通模块拆分
modules.forEach(mod => {
  if (zh[mod]) {
    fs.writeFileSync(
      path.join(outDir, `${mod}.json`),
      JSON.stringify({ [mod]: zh[mod] }, null, 2),
      'utf8'
    );
    console.log(`模块 ${mod} 拆分完成`);
  }
});

// menu 拆分
let menuObj = zh;
for (const k of menuPath) {
  menuObj = menuObj && menuObj[k];
}
if (menuObj) {
  fs.writeFileSync(
    path.join(outDir, `${menuKey}.json`),
    JSON.stringify({ [menuKey]: menuObj }, null, 2),
    'utf8'
  );
  console.log('模块 menu 拆分完成');
}

console.log('全部拆分完成！');
