const fs = require('fs');
const path = require('path');

// 后端 types 目录
const typesDir = path.join(__dirname, '../../src/main/java/com/fwai/turtle/types');
// 输出 i18n 文件
const outFile = path.join(__dirname, '../src/assets/i18n/zh-CN/types.json');

function parseEnumFile(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const enumMatch = content.match(/enum\s+(\w+)\s*\{([\s\S]*?)\}/);
  if (!enumMatch) return null;
  const enumName = enumMatch[1];
  const enumBody = enumMatch[2];
  const values = {};
  // 匹配 ONBOARDING("入职培训") 或 ONBOARDING("入职培训", 1)
  const itemRegex = /([A-Z0-9_]+)\s*\(\s*"([^"]+)"/g;
  let m;
  while ((m = itemRegex.exec(enumBody)) !== null) {
    const key = m[1].toLowerCase();
    const label = m[2];
    values[key] = label;
  }
  return { enumName, values };
}

const result = { types: {} };

fs.readdirSync(typesDir).forEach(file => {
  if (!file.endsWith('.java')) return;
  const parsed = parseEnumFile(path.join(typesDir, file));
  if (parsed) {
    result.types[parsed.enumName.toLowerCase()] = parsed.values;
  }
});

fs.writeFileSync(outFile, JSON.stringify(result, null, 2), 'utf8');
console.log('types.json 已生成:', outFile);
