package edu.ub.pis2016.pis16.strikecom.engine.physics;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Bevzenko
 * as for now - axis-bound rectangles only
 * todo: include other shapes after testing this
 */
public class SpatialHashGrid {
	List<RectangleObject>[] dynamicCells;
	List<RectangleObject>[] staticCells;
	int cellsPerRow;
	int cellsPerCol;
	float cellSize;
	int[] cellIds=new int[4];
	List<RectangleObject> foundObjects;

	/**
	 * constructor takes world Width, Height and cell size and divides world into small cells, and initialises all lists
	 * @param worldWidth world width
	 * @param worldHeight world height
	 * @param cellSize size of a cell
	 */
	public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize){
		this.cellSize=cellSize;
		this.cellsPerRow=(int)Math.ceil(worldWidth/cellSize);
		this.cellsPerCol=(int)Math.ceil(worldHeight/cellSize);
		int numCells=this.cellsPerRow*this.cellsPerCol;
		dynamicCells= new List[numCells];
		staticCells = new List[numCells];
		for (int i=0; i<numCells;i++){
			dynamicCells[i]=new ArrayList<RectangleObject>(5);
			staticCells[i]=new ArrayList<RectangleObject>(10);
		}
		foundObjects= new ArrayList<RectangleObject>(10);
	}
	public void insertStaticObject(RectangleObject obj){
		int [] cellIds= getCellIds(obj); // test
		int i = 0;
		int cellId=-1;
		while(i<=3&&(cellId=cellIds[i++])!=-1){
			this.staticCells[cellId].add(obj);
		}
	}
	public void insertDynamicObject(RectangleObject obj){
		int [] cellIds= getCellIds(obj); // test
		int i = 0;
		int cellId=-1;
		while(i<=3&&(cellId=cellIds[i++])!=-1){
			this.dynamicCells[cellId].add(obj);
		}
	}

	public void removeObject(RectangleObject obj){
		int [] cellIds= getCellIds(obj); // test
		int i = 0;
		int cellId=-1;
		while(i<=3&&(cellId=cellIds[i++])!=-1){
			this.staticCells[cellId].remove(obj);
			this.dynamicCells[cellId].remove(obj);
		}
	}

	/**
	 * is called each frame in order to update dynamic objects positions correctly
	 */
	public void clearDynamicCells(){
		for(int i =0;i<this.dynamicCells.length;i++){
			this.dynamicCells[i].clear();
		}
	}

	public List<RectangleObject> getPotentialColliders(RectangleObject obj){
		this.foundObjects.clear();
		int [] cellIds= getCellIds(obj);
		int i = 0;
		int cellId=-1;
		while(i<=3&&(cellId=cellIds[i++])!=-1){
			for(int j =0;i<this.dynamicCells.length;j++){
				RectangleObject collider=this.dynamicCells[cellId].get(j);
				if(!this.foundObjects.contains(collider)){
					this.foundObjects.add(collider);
				}
			}
			for(int j =0;i<this.staticCells.length;j++){
				RectangleObject collider=this.staticCells[cellId].get(j);
				if(!this.foundObjects.contains(collider)){
					this.foundObjects.add(collider);
				}
			}

		}
		return this.foundObjects;
	}
	public int[] getCellIds(RectangleObject obj){
		int x1 = (int)Math.floor(obj.getBounds().getLlpos().x/this.cellSize);
		int y1 = (int)Math.floor(obj.getBounds().getLlpos().y/this.cellSize);
		int x2 = (int)Math.floor((obj.getBounds().getLlpos().x+obj.getBounds().getWidth())/this.cellSize);
		int y2 = (int)Math.floor((obj.getBounds().getLlpos().y+obj.getBounds().getHeight())/this.cellSize);
		if(x1==x2 && y1==y2){
			if(x1>=0 && x1<this.cellsPerRow && y1>=0 && y1<this.cellsPerCol){
				this.cellIds[0]=x1+y1*this.cellsPerRow;
			}
			else{
				this.cellIds[0]=-1;
				this.cellIds[1]=-1;
				this.cellIds[2]=-1;
				this.cellIds[3]=-1;
			}
		}
		else if (x1==x2){
			int i = 0;
			if(x1>=0 && x1< this.cellsPerRow){
				if(y1>=0 && y1<this.cellsPerCol){
					this.cellIds[i++]= x1+y1*this.cellsPerRow;
				}
				if(y2>=0 && y2<this.cellsPerCol){
					this.cellIds[i++]= x1+y1*this.cellsPerRow;
				}
			}
			while(i<4) this.cellIds[i++]=-1;
		}
		else if (y1==y2){
			int i = 0;
			if(y1>=0 && y1< this.cellsPerCol){
				if(x1>=0 && x1<this.cellsPerRow){
					this.cellIds[i++]= x1+y1*this.cellsPerRow;
				}
				if(x2>=0 && x2<this.cellsPerRow){
					this.cellIds[i++]= x1+y1*this.cellsPerRow;
				}
			}
			while(i<4) this.cellIds[i++]=-1;
		}
		else{
			int i = 0;
			int y1CellsPerRow=y1*cellsPerRow;
			int y2CellsPerRow=y2*cellsPerRow;
			if(x1>=0 && x1<this.cellsPerRow && y1>=0 && y1<this.cellsPerCol){
				this.cellIds[i++]=x1+y1CellsPerRow;
			}
			if(x2>=0 && x2<this.cellsPerRow && y1>=0 && y1<this.cellsPerCol){
				this.cellIds[i++]=x2+y1CellsPerRow;
			}
			if(x2>=0 && x2<this.cellsPerRow && y2>=0 && y2<this.cellsPerCol){
				this.cellIds[i++]=x2+y2CellsPerRow;
			}
			if(x1>=0 && x1<this.cellsPerRow && y2>=0 && y2<this.cellsPerCol){
				this.cellIds[i++]=x1+y2CellsPerRow;
			}
			while(i<4) this.cellIds[i++]=-1;
		}
		return this.cellIds;
	}

}
