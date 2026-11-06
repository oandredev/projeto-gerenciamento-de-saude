/* ---------- Ícones (SVG inline) ---------- */
const P={
 help:'<circle cx="12" cy="12" r="9"/><path d="M9.5 9.5a2.5 2.5 0 1 1 3.5 2.3c-.7.4-1 .9-1 1.7M12 17h.01"/>',
 finger:'<path d="M6 10a6 6 0 0 1 12 0v2M9 20c0-3 0-6 3-6s3 3 3 6M12 10v4M6 14v2M18 13v3"/>',
 pill:'<rect x="3" y="8" width="18" height="8" rx="4" transform="rotate(-45 12 12)"/><path d="M9 9l6 6"/>',
 check:'<path d="M5 12l5 5 9-10"/>',
 checkc:'<circle cx="12" cy="12" r="9"/><path d="M8 12l3 3 5-6"/>',
 time:'<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>',
 cal:'<rect x="4" y="5" width="16" height="15" rx="2"/><path d="M4 10h16M8 3v4M16 3v4"/>',
 back:'<path d="M19 12H5M11 6l-6 6 6 6"/>',
 bell:'<path d="M6 16v-5a6 6 0 0 1 12 0v5l2 2H4zM10 21h4"/>',
 swap:'<path d="M4 8h14l-4-4M20 16H6l4 4"/>',
 add:'<path d="M12 5v14M5 12h14"/>',
 loc:'<path d="M12 21s7-6 7-11a7 7 0 0 0-14 0c0 5 7 11 7 11z"/><circle cx="12" cy="10" r="2.5"/>',
 steth:'<path d="M6 3v6a4 4 0 0 0 8 0V3M10 13v2a5 5 0 0 0 10 0v-2"/><circle cx="20" cy="11" r="2"/>',
 home:'<path d="M4 11l8-7 8 7v9H4z"/>'
};
const icon=(n,s=24)=>`<svg class="i" width="${s}" height="${s}" viewBox="0 0 24 24">${P[n]}</svg>`;
const logo=(s=44)=>`<svg width="${s}" height="${s}" viewBox="0 0 60 60"><rect width="60" height="60" rx="18" fill="#fff"/><rect x="24" y="12" width="12" height="36" rx="3" fill="#1636A8"/><rect x="12" y="24" width="36" height="12" rx="3" fill="#1636A8"/><polyline points="8,32 20,32 25,22 32,42 37,32 52,32" fill="none" stroke="#F57C00" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/></svg>`;

/* ---------- Utilitários (equivalente a utils/format) ---------- */
const pad=n=>String(n).padStart(2,'0');
const formatTime=d=>{d=new Date(d);return `${pad(d.getHours())}:${pad(d.getMinutes())}`};
const formatDate=d=>new Date(d).toLocaleDateString('pt-BR',{day:'2-digit',month:'short'}).replace('.','');
const STRIPE_LABELS={NONE:'Sem tarja',RED:'Tarja vermelha',BLACK:'Tarja preta',OTHER:'Outra tarja'};
const UNIT_MIN={MINUTE:1,HOUR:60,DAY:1440};
// Horários do dia a partir do início e do intervalo (ex.: 08:00 a cada 12 h -> 08:00, 20:00)
function getDailyTimes(startAt,value,unit){
  const step=value*(UNIT_MIN[unit]||0);
  const s=new Date(startAt);
  const first=s.getHours()*60+s.getMinutes();
  if(!step||step>=1440)return [formatTime(s)];
  const out=[];
  for(let m=first%step;m<1440;m+=step)out.push(`${pad(Math.floor(m/60))}:${pad(m%60)}`);
  return out;
}
const joinTimes=a=>a.join(' · ');

/* ---------- Dados de exemplo (mockData.js) ---------- */
const todayAt=(h,m=0)=>{const d=new Date();d.setHours(h,m,0,0);return d.toISOString()};
const daysFromNow=(n,h,m=0)=>{const d=new Date();d.setDate(d.getDate()+n);d.setHours(h,m,0,0);return d.toISOString()};

// GET /occurrence
const occurrences=[
 {confirmationId:1,scheduleId:1,title:'Mebendazol 20 mg',type:'MEDICATION',scheduledAt:todayAt(8),status:'CONFIRMED',confirmedAt:todayAt(8,5)},
 {confirmationId:2,scheduleId:2,title:'Losartana 50 mg',type:'MEDICATION',scheduledAt:todayAt(9,30),status:'CONFIRMED',confirmedAt:todayAt(9,32)},
 {confirmationId:3,scheduleId:3,title:'Insulina',type:'MEDICATION',scheduledAt:todayAt(12),status:'CONFIRMED',confirmedAt:todayAt(12,3)},
 {confirmationId:4,scheduleId:4,title:'Omeprazol 20 mg',type:'MEDICATION',scheduledAt:todayAt(14),status:'PENDING',confirmedAt:null},
 {confirmationId:5,scheduleId:5,title:'Dr. Hanschucrutz - UBS Santana',type:'CONSULTATION',scheduledAt:todayAt(16,30),status:'PENDING',confirmedAt:null},
 {confirmationId:6,scheduleId:6,title:'Sinvastatina 20 mg',type:'MEDICATION',scheduledAt:todayAt(18),status:'PENDING',confirmedAt:null},
 {confirmationId:7,scheduleId:7,title:'Metformina 850 mg',type:'MEDICATION',scheduledAt:todayAt(20),status:'PENDING',confirmedAt:null},
 {confirmationId:8,scheduleId:8,title:'Vitamina D',type:'MEDICATION',scheduledAt:todayAt(22),status:'SCHEDULE',confirmedAt:null},
];

// GET /schedule?type=MEDICATION
const medications=[
 {id:1,type:'MEDICATION',title:'Omeprazol 20 mg',startAt:todayAt(8),intervalValue:12,intervalUnit:'HOUR',
  details:{brand:'EMS',medicineName:'Omeprazol',activeIngredient:'omeprazol',dosageAmount:20,dosageUnit:'MG',stripe:'NONE'}},
 {id:2,type:'MEDICATION',title:'Metformina 850 mg',startAt:todayAt(20),intervalValue:24,intervalUnit:'HOUR',
  details:{brand:'Medley',medicineName:'Metformina',activeIngredient:'metformina',dosageAmount:850,dosageUnit:'MG',stripe:'RED'}},
];

// GET /schedule?type=CONSULTATION
const appointments=[
 {id:10,type:'CONSULTATION',title:'Consulta de rotina',startAt:daysFromNow(4,16,30),intervalUnit:'UNIQUE',
  details:{doctorName:'Dr. Hanschucrutz',location:'UBS Santana'}},
 {id:11,type:'CONSULTATION',title:'Retorno',startAt:daysFromNow(9,9,0),intervalUnit:'UNIQUE',
  details:{doctorName:'Dra. Marina Alves',location:'Hospital São Luiz'}},
];

/* ---------- Componentes ---------- */
const header=(title,{back=false,profile=true}={})=>`
<div><div class="hdr"><div class="top"><div class="left">
 ${back?`<button class="circle" data-go="login">${icon('back')}</button>`:''}${logo()}</div>
 <button class="circle">${icon('bell')}</button></div>
 <h1>${title}</h1>
 ${profile?`<button class="profile">${icon('swap',20)}<span>Trocar perfil</span></button>`:''}
</div><div class="line"></div></div>`;

const tabs=cur=>`<nav class="tabs">${[['home','Início','home'],['meds','Remédios','pill'],['appts','Consultas','steth']]
 .map(([k,l,i])=>`<button class="tab ${cur===k?'on':''}" data-go="${k}">${icon(i)}<span>${l}</span></button>`).join('')}</nav>`;

const shell=(cur,body)=>`${body}${tabs(cur)}`;

/* ---------- Telas ---------- */
const screens={
login:()=>`
<div class="login-head">${logo()}<div class="txt"><div class="brand">A<span>&amp;</span>F</div><div class="sub">Health Management</div></div></div>
<div class="line"></div>
<div class="scroll pad">
 <div class="help">${icon('help',32)}<div>Dúvidas no acesso? Veja o passo a passo.<a href="#">Acessar manual.</a></div></div>
 <label>E-mail</label><input type="email" placeholder="Digite seu e-mail" autocapitalize="none">
 <div class="pw"><label>Senha</label><a href="#">Esqueci minha senha</a></div>
 <input type="password" placeholder="Digite sua senha">
 <button class="btn fill" style="margin-top:8px"><span>Entrar com biometria</span>${icon('finger',22)}</button>
 <button class="btn ob" data-go="home">Entrar</button>
 <p class="reg">Ainda não é cadastrado?</p>
 <button class="btn oo" data-go="signup">Fazer cadastro</button>
</div>`,

signup:()=>`${header('Cadastro',{back:true,profile:false})}<div class="scroll"></div>`,

home:()=>{
 const occ=[...occurrences].sort((a,b)=>new Date(a.scheduledAt)-new Date(b.scheduledAt));
 const now=occ.find(o=>o.type==='MEDICATION'&&o.status==='PENDING');
 const rest=occ.filter(o=>o!==now);
 const chip=o=>{
  const m={CONFIRMED:['checkc','blue'],PENDING:['time','or'],SCHEDULE:['cal','blue']}[o.status];
  const l=o.status==='PENDING'?'Pendente':o.status==='SCHEDULE'?'Agendado':(o.type==='MEDICATION'?'Tomado':'Realizado');
  return `<span class="chip ${m[1]}">${icon(m[0],20)}${l}</span>`;};
 return shell('home',`${header('Boa tarde, Fernanda!')}<div class="scroll content">
 ${now?`<div class="lbl" style="color:var(--orangeText)">Agora</div>
 <div class="now"><div class="row"><div class="box">${icon('pill',28)}</div>
  <div class="t"><div class="nm">${now.title}</div><div class="dt">Às ${formatTime(now.scheduledAt)}</div></div></div>
  <button class="btn fill taken" data-taken="${now.confirmationId}"><span>Já tomei</span>${icon('check',22).replace('<svg','<svg style="color:#fff"')}</button></div>`:''}
 <div class="lbl" style="color:var(--blue)">Hoje</div>
 ${rest.map(i=>`<div class="item"><div class="top"><span class="tm">${formatTime(i.scheduledAt)}</span>${chip(i)}</div><div class="nm">${i.title}</div></div>`).join('')}
 </div>`);
},

meds:()=>shell('meds',`${header('Remédios')}<div class="scroll content" style="padding-bottom:16px">
 ${medications.map(m=>{const n=m.details.stripe==='NONE';return `
 <div class="card"><div class="box big">${icon('pill',30)}</div><div class="t">
  <div class="nm">${m.title}</div>
  <div class="d" style="margin-top:4px">Princípio ativo: ${m.details.activeIngredient}</div>
  <div class="d" style="margin-top:2px">${icon('time',20)}<span>${joinTimes(getDailyTimes(m.startAt,m.intervalValue,m.intervalUnit))}</span></div>
  <span class="tag ${n?'n':'s'}">${STRIPE_LABELS[m.details.stripe]??STRIPE_LABELS.OTHER}</span></div></div>`}).join('')}
 </div><div class="foot"><button class="btn fill">${icon('add',26).replace('<svg','<svg style="color:#fff"')}<span>Novo remédio</span></button></div>`),

appts:()=>shell('appts',`${header('Consultas')}<div class="scroll content" style="padding-bottom:16px">
 ${appointments.map(a=>`
 <div class="card"><div class="box big">${icon('steth',30)}</div><div class="t">
  <div class="nm">${a.details.doctorName}</div>
  <div class="d">${icon('loc',20)}<span>${a.details.location}</span></div>
  <div class="d">${icon('cal',20)}<span>${formatDate(a.startAt)} · ${formatTime(a.startAt)}</span></div></div></div>`).join('')}
 </div><div class="foot"><button class="btn fill">${icon('add',26).replace('<svg','<svg style="color:#fff"')}<span>Nova consulta</span></button></div>`)
};

/* ---------- Navegação ---------- */
const app=document.getElementById('app');
function go(name){app.innerHTML=screens[name]();}
app.addEventListener('click',e=>{
  const g=e.target.closest('[data-go]');
  if(g)return go(g.dataset.go);
  const t=e.target.closest('[data-taken]');
  if(t){
    const o=occurrences.find(x=>x.confirmationId==t.dataset.taken);
    console.log('Já tomei, confirmationId:',o.confirmationId);
    o.status='CONFIRMED';go('home');
  }
});
go('login');
