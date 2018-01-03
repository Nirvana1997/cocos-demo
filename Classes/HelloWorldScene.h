#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"

using namespace cocos2d;

class HelloWorld : public cocos2d::Scene
{
public:
    static Scene* createScene();

    virtual bool init();
    
    // a selector callback
    void menuCloseCallback(Ref* pSender);
    
    void addMonster(float dt);
    
    bool onTouchBegan(Touch *touch, Event *unused_event);
    
    void moveFinish(Node* sender);
    
    void update(float dt);
    
    // implement the "static create()" method manually
    CREATE_FUNC(HelloWorld);
    
private:
    Sprite* _player;
    Array* _monsters;
    Array* _fireballs;
    std::map<EventKeyboard::KeyCode,bool> keys;
    
    const int OFFSET = 4;
};

#endif // __HELLOWORLD_SCENE_H__
