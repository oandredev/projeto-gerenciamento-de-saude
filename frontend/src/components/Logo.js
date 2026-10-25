import Svg, { Rect, Polyline } from 'react-native-svg';

export default function Logo({ size = 96, inverted = false }) {
  const bg = inverted ? '#1636A8' : '#FFFFFF';
  const cross = inverted ? '#FFFFFF' : '#1636A8';

  return (
    <Svg width={size} height={size} viewBox="0 0 60 60">
      <Rect x="0" y="0" width="60" height="60" rx="18" fill={bg} />
      <Rect x="24" y="12" width="12" height="36" rx="3" fill={cross} />
      <Rect x="12" y="24" width="36" height="12" rx="3" fill={cross} />
      <Polyline
        points="8,32 20,32 25,22 32,42 37,32 52,32"
        fill="none"
        stroke="#F57C00"
        strokeWidth="3"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </Svg>
  );
}