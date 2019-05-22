import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.engine.display.Bitmap;
import com.engine.display.CPanel;
import com.engine.display.Frame;
import com.engine.display.MovieClip;
import com.engine.display.Stage;
import com.engine.display.Text;
import com.engine.event.EventListener;
import com.engine.event.StageEvent;

public class fripbird  extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static CPanel panel = new CPanel(320, 416);
	public Map<String, Image> images = new  HashMap<String, Image>();
	public ArrayList<Bitmap> items = new  ArrayList<Bitmap>();
	public MovieClip hero;
	
	public Bitmap bg1Sprite, bg2Sprite, road1Sprite, road2Sprite,tipBitmap, startlogoBitmp, overBitmap, resultBitmap, newBitmap, startlogoBitmap;

	public boolean isUp = true,isStart = false,isOver = false;
	int step = 80,stepindex = 75,moveStep = 0,score = 0,record = 0;
	double v = 1;
	public Stage stage;
	public Text pointTxt, rpointTxt, npointTxt;
	public fripbird() {
		setTitle(" fripbird");
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(320, 446);
		setLocationRelativeTo(null);
		setVisible(true);
		this.init();
		
	}


	public static void main(String[] args) {
		new fripbird();
		
	}
	public void init(){
		this.loadImage();
		this.stage=new Stage(panel);
		Bitmap back=new Bitmap(this.images.get("bg"));
		back.mouseEnabled=true;
		this.stage.addChild(back);
		hero=new MovieClip();
		hero.addFrame(new Frame(new Bitmap(this.images.get("bird"),0,0,34,24),null,0,0,false));
		hero.addFrame(new Frame(new Bitmap(this.images.get("bird"),34,0,34,24),null,0,0,false));
		hero.addFrame(new Frame(new Bitmap(this.images.get("bird"),68,0,34,24),null,0,0,false));
        hero.x = 75;
        hero.y = 200;
        pointTxt=new Text("0");
        pointTxt.font=new Font("Arial",Font.BOLD,24);
        pointTxt.visible = false;
        pointTxt.x = 0;
        pointTxt.y = 10;
		stage.addChild(pointTxt);
		stage.addChild(hero);

		road1Sprite=new Bitmap(this.images.get("road"));
	    road1Sprite.x = 320 - 336;
	    road1Sprite.y = 360;
	    stage.addChild(road1Sprite);
	    
	    road2Sprite=new Bitmap(this.images.get("road"));
	    road2Sprite.x = 320 - 336 - 336;
	    road2Sprite.y = 360;
	    stage.addChild(road2Sprite);
	    
	    tipBitmap = new Bitmap(this.images.get("tip"));
	    tipBitmap.x = 103;
	    tipBitmap.y = 200;
	    stage.addChild(tipBitmap);
	    
	    startlogoBitmap = new Bitmap(this.images.get("startlogo"));
	    startlogoBitmap.x = 66;
	    startlogoBitmap.y = 80;
	    stage.addChild(startlogoBitmap);
	    
		back.addEventListener("mousedown", new EventListener(){
			public void somethingDo(StageEvent e){
				bgClick();
			}
		});
		back.addEventListener("enterframe", new EventListener(){
			public void somethingDo(StageEvent e){
				onframe();
			}
		});
	}
	
	public void bgClick() {
	    if (!isOver) {
	        if (!isStart) {
	            stage.removeChild(startlogoBitmap);
	            stage.removeChild(tipBitmap);
	            pointTxt.visible = true;
	            pointTxt.text = "0";
	            isStart = true;
	        };
	        hero.rotation = -30;
	        v = -7;
	    } else {
	        if (moveStep > 17) {
	            stage.removeChild(overBitmap);
	            stage.removeChild(resultBitmap);
	            stage.removeChild(rpointTxt);
	            stage.removeChild(npointTxt);
	            stage.removeChild(newBitmap);
	            for(int i=0;i<items.size();i++){
	            	stage.removeChild(items.get(i));
	            }
	            items = new  ArrayList<Bitmap>();
	            step = 80;
	            stepindex = 75;
	            score = 0;
	            pointTxt.text = "0";
	            pointTxt.visible = false;
	            tipBitmap = new Bitmap(this.images.get("tip"));
	            tipBitmap.x = 103;
	            tipBitmap.y = 200;
	            stage.addChild(tipBitmap);
	            startlogoBitmap = new Bitmap(this.images.get("startlogo"));
	            startlogoBitmap.x = 66;
	            startlogoBitmap.y = 80;
	            stage.addChild(startlogoBitmap);
	            hero.paused=false;
	            hero.x = 75;
	            hero.y = 200;
	            hero.rotation = 0;
	            isStart = false;
	            isOver = false;
	            moveStep = 0;
	        }
	    }
	};
	
	
	public void onframe() {
		if (!isOver) {
			road2Sprite.x += 1;
			if (road2Sprite.x >= 320) {
				road2Sprite.x = 320 - 336 - 336 + 1;
			}
			road1Sprite.x += 1;
			if (road1Sprite.x >= 320) {
				road1Sprite.x = 320 - 336 - 336 + 1;
			}
			if (!isStart) {
				if (hero.y <= 180) {
					isUp = true;
				}
				if (hero.y >= 215) {
					isUp = false;
				}
				if (isUp) {
					hero.y++;
				} else {
					hero.y--;
				}
			}
			if (isStart) {
				hero.y += v;
				if (hero.y <= 0) {
					hero.y = 0;
				}
				v += 0.6;
				if (v > 0) {
					hero.rotation += 2;
				}
				if (hero.y > 360 - 34) {
					isOver = true;
				}
				for (int i = 0; i < items.size(); i++) {
					items.get(i).x -= 2;
					if (items.get(i).hitTestObject(hero, true, 127)) {
						isOver = true;
					}
					if (items.get(i).x == 22 && items.get(i).name == "up") {
						score += 1;
						pointTxt.text = "" + score;
					}
					if (items.get(i).x < -52) {
						stage.removeChild(items.get(i));
						items.remove(i);
						i--;
					}
				}

				if (stepindex++ > step) {
					stepindex = 0;
					int speed = (int) Math.floor(Math.random() * 150);
					Bitmap item = new Bitmap(this.images.get("up"));
					item.name = "up";
					item.y = 170 + speed;
					item.x = 320 + 52;
					items.add(item);
					stage.addChildAt(item, 1);
					Bitmap item1 = new Bitmap(this.images.get("down"));
					item1.y = -262 + speed;
					item1.x = 320 + 52;
					item1.name = "down";
					items.add(item1);
					stage.addChildAt(item1, 1);

				}

			}
		} else {
			hero.stop();
			moveStep += 1;
			if (moveStep <= 16) {
				if (moveStep % 6 == 0) {
					hero.y += 2;
					hero.x += 2;
					for (int i = 1; i < stage.children.size(); i++) {
						stage.children.get(i).y += 2;
						stage.children.get(i).x += 2;
					}
				} else if (moveStep % 6 == 3) {
					hero.y -= 2;
					hero.x -= 2;
					for (int i = 1; i < stage.children.size(); i++) {
						stage.children.get(i).y -= 2;
						stage.children.get(i).x -= 2;
					}
				}
			}
			if (moveStep == 17) {
				overBitmap = new Bitmap(this.images.get("over"));
				overBitmap.x = 64;
				overBitmap.y = 80;
				stage.addChild(overBitmap);
				resultBitmap = new Bitmap(this.images.get("result"));
				resultBitmap.x = 47;
				resultBitmap.y = 151;
				stage.addChild(resultBitmap);
				rpointTxt = new Text("" + score);
				rpointTxt.x = 242 - 16 * rpointTxt.text.length();
				rpointTxt.y = 182;
				stage.addChild(rpointTxt);
				npointTxt = new Text("" + record);
				npointTxt.x = 242 - 16 * npointTxt.text.length();
				npointTxt.y = 224;
				stage.addChild(npointTxt);
				if (score > record) {
					record=score;
					newBitmap = new Bitmap(this.images.get("new"));
					newBitmap.x = 172;
					newBitmap.y = 160;
					stage.addChild(newBitmap);
				}
			}
		}
	}
	public  void loadImage(){
		String[] imageData = new String[]{
			     "bg",
			     "./images/bg.png",
			     "bird",
			     "./images/bird.png",
			     "down",
			     "./images/down.png",
			     "up",
			     "./images/up.png",
			     "road",
			     "./images/road.png",
			     "startlogo",
			     "./images/startlogo.png",
			     "new",
			     "./images/new.png",
			     "result",
			     "./images/result.png",
			     "over",
			     "./images/over.png",
			     "tip",
			     "./images/tip.png"
			};
		for(int i=0;i<imageData.length;i+=2){
			this.images.put(imageData[i],new ImageIcon(imageData[i+1]).getImage());
		}
	}
}
